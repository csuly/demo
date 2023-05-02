import pandas as pd
import numpy as np
import mysql.connector
from scipy.spatial.distance import directed_hausdorff
import sys

#读取数据库
def getDate(source):
  # 建立数据库连接
  mydb = mysql.connector.connect(
      host="gz-cynosdbmysql-grp-5dm79phb.sql.tencentcdb.com:27114",
      user="ghostlee",
      password="Ly200210",
      auth_plugin="mysql_native_password",
      database="data"
  )

  # 创建游标对象
  mycursor = mydb.cursor()

    # 执行SQL查询
  mycursor.execute(
    "SELECT source,batch,time_min,time_max,duration,min_lon,max_lon,min_lat,max_lat,avg_vel,avg_accel,avg_cou,avg_anguvel,points,sparsity"+
    " FROM `2802-features-normalized`"+
    " WHERE source="+source)

  # 获取查询结果
  myresult = mycursor.fetchall()
  result=pd.DataFrame(list(myresult))
  result.columns=[
    'source','batch','time_min','time_max','duration',
    'min_lon','max_lon','min_lat','max_lat','avg_vel',
    'avg_accel','avg_cou','avg_anguvel','points','sparsity',
  ]
  # 打印查询结果
  mycursor.close()
  mydb.close()
  return result

 # 计算两信源关联结果
def association(batch1, batch2, batch_num1, batch_num2, feature_1, feature_2, columns, source1, source2, weights):
    # 初始化距离矩阵D和关联矩阵A
    D = np.zeros((batch_num1, batch_num2))  # 距离矩阵
    A = np.zeros((batch_num1, batch_num2))  # 关联矩阵

    # 2.1 欧氏距离计算
    # 通过两层循环计算两信源间各批号航迹欧氏距离，并存入距离矩阵
    for i, b1 in feature_1.iterrows():
        for j, b2 in feature_2.iterrows():
            # i/j用于为矩阵填值；b1/b2表示每个信源下的当前航迹，用于计算特征欧氏距离
            dis = 0
            for k, f in enumerate(columns):
                dis = dis + ((b1[f] - b2[f]) ** 2) * weights[k]
            D[i, j] = dis

    # 创建一个字典，按顺序存储关联航迹的批号
    dic = {
        f'{source1}': [],
        f'{source2}': []
    }

    # 关联矩阵填充
    min_batch_num = batch_num1 if (batch_num1 <= batch_num2) else batch_num2
    for n in range(min_batch_num):
        min_index = np.unravel_index(D.argmin(), D.shape)  # 获取最小元素索引
        A[min_index[0], min_index[1]] = 1  # 将关联矩阵对应位置设置为1

        # 向字典中记录关联结果
        dic[f'{source1}'].append(batch1[min_index[0]])
        dic[f'{source2}'].append(batch2[min_index[1]])

        # 将距离矩阵对应位置元素所在行列的所有元素都设置为正无穷
        D[[min_index[0]], :] = np.inf
        D[:, [min_index[1]]] = np.inf

    a_assn = pd.DataFrame(columns=[f'{source1}', f'{source2}'])
    a_assn[f'{source1}'] = dic[f'{source1}']
    a_assn[f'{source2}'] = dic[f'{source2}']
    return (a_assn)


"""
关联判断流程：
1.读取两信源特征文件
2.对所有航迹进行特征值欧式距离计算，测试各特征值权重
3.输出关联结果，找到最优结果
"""
scene_index = 2802  # 定义场景文件序号

# 1.读取两信源特征文件
feature_9001=getDate("9001")
feature_9002=getDate("9002")
feature_9003=getDate("9003")


# 2.对所有航迹进行特征值欧式距离计算
columns = list(feature_9001.columns)  # 统计所有特征名
columns.pop(0)
columns.pop(0)
columns.remove('points')
columns.remove('sparsity')

# 创建权重列表
weights=[]
for item in sys.argv[1:]:
    weights.append(float(item))
#print(weights)

# 获取信源所有批号
batch_9001 = feature_9001['batch'].unique().tolist()  # 9001所有批号
batch_9002 = feature_9002['batch'].unique().tolist()  # 9002所有批号
batch_9003 = feature_9003['batch'].unique().tolist()  # 9003所有批号
# print(batch_9003)

# 获取信源批号数量
batch_9001_num = len(batch_9001)
batch_9002_num = len(batch_9002)
batch_9003_num = len(batch_9003)

# 9001和9002的关联结果
assn_1_2 = association(batch_9001, batch_9002, batch_9001_num, batch_9002_num, feature_9001, feature_9002, columns,
                       9001, 9002, weights)
# 9001和9003的关联结果
assn_1_3 = association(batch_9001, batch_9003, batch_9001_num, batch_9003_num, feature_9001, feature_9003, columns,
                       9001, 9003, weights)

assn_all = pd.DataFrame(columns=['9001'])
assn_all['9001'] = batch_9001
assn_all = pd.merge(assn_all, assn_1_2, how='left', on='9001')
assn_all = pd.merge(assn_all, assn_1_3, how='left', on='9001')

# 将nan使用-1填充
assn_all = assn_all.where((assn_all.notna()), -1)
assn_all = assn_all.reset_index(drop=True)
#print(assn_all)

# 找到没有被匹配到的航迹，添加到列表尾
diff_9002 = list(set(batch_9002) - set(assn_all['9002'].tolist()))
#print(diff_9002)

diff_9003 = list(set(batch_9003) - set(assn_all['9003'].tolist()))
#print(diff_9003)

# 如果有多余航迹，就把他们放进列表
if diff_9002 != []:
    df_piece = pd.DataFrame(columns=['9001', '9002', '9003'])
    df_piece['9002'] = diff_9002
    df_piece = df_piece.where((df_piece.notna()), -1)
    #print(df_piece)
    assn_all = assn_all.append(df_piece, ignore_index=True)

#显示所有行
pd.set_option('display.max_rows', None)
for row in range(1,len(assn_all)):
    print(str(assn_all.iloc[row][0])+','+str(assn_all.iloc[row][1]))
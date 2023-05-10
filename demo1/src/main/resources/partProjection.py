import io
import mysql.connector
import numpy as np
import pandas as pd
from sklearn.neighbors import NearestNeighbors
from sklearn.manifold import Isomap
from sklearn.decomposition import KernelPCA
from sklearn.metrics import pairwise_distances
from sklearn.manifold import MDS
from sklearn.decomposition import PCA
from sklearn.manifold import TSNE
from sklearn.discriminant_analysis import LinearDiscriminantAnalysis
import umap.umap_ as umap
from sklearn.preprocessing import MinMaxScaler
import sys

def createTable(target_scene):
    # 建立数据库连接
    mydb = mysql.connector.connect(
        host="gz-cynosdbmysql-grp-5dm79phb.sql.tencentcdb.com",
        port=27114,
        user="ghostlee",
        password="Ly200210",
        auth_plugin="mysql_native_password",
        database="data"
        # host="127.0.0.1",
        # port=3306,
        # user="root",
        # password="Fys8211200417",
        # database="shitai_db"
    )
    # 创建游标对象
    mycursor = mydb.cursor()
    # 执行SQL查询
    mycursor.execute("DROP TABLE IF EXISTS `"+str(target_scene)+"-features-projection`;")
    mycursor.execute(
        "CREATE TABLE `"+str(target_scene)+"-features-projection` ("+ \
        "id int NOT NULL AUTO_INCREMENT,"+"source int,"+"batch int,"+"time_min float,"+"time_max float,"+ \
        "duration float,"+"min_lon float,"+"max_lon float,"+"min_lat float,"+ \
        "max_lat float,"+"avg_vel float,"+"avg_accel float,"+"avg_cou float,"+ \
        "avg_anguvel float,"+"points float,"+"sparsity float,"+ \
        "projection_x_ISOMAP float,"+"projection_y_ISOMAP float,"+ \
        "projection_x_KPCA float,"+"projection_y_KPCA float,"+ \
        "projection_x_MDS float,"+"projection_y_MDS float,"+ \
        "projection_x_PCA float,"+"projection_y_PCA float,"+ \
        "projection_x_TSNE float,"+"projection_y_TSNE float,"+ \
        "projection_x_UMAP float,"+"projection_y_UMAP float,"+ \
        "projection_x_LDA float,"+"projection_y_LDA float,"+ \
        "PRIMARY KEY (id) );"
    )

def getDate(target_scene):
    # 建立数据库连接
    mydb = mysql.connector.connect(
        host="gz-cynosdbmysql-grp-5dm79phb.sql.tencentcdb.com",
        port=27114,
        user="ghostlee",
        password="Ly200210",
        auth_plugin="mysql_native_password",
        database="data"
        # host="127.0.0.1",
        # port=3306,
        # user="root",
        # password="Fys8211200417",
        # database="shitai_db"
    )

    # 创建游标对象
    mycursor = mydb.cursor()

    # 执行SQL查询
    mycursor.execute(
        "SELECT id,source,batch,time_min,time_max,duration,min_lon,max_lon,min_lat,max_lat,avg_vel,avg_accel,avg_cou,avg_anguvel,points,sparsity"
        +" FROM `"+str(target_scene)+"-features-normalized`")

    # 获取查询结果
    myresult = mycursor.fetchall()
    result=pd.DataFrame(list(myresult))
    result.columns=['id','source','batch','time_min','time_max','duration','min_lon','max_lon','min_lat','max_lat','avg_vel','avg_accel','avg_cou','avg_anguvel','points','sparsity']
    # 打印查询结果
    #print(result)
    mycursor.close()
    mydb.close()
    return result

#将数据存储到数据库
def saveDate(info,target_scene):
    # 建立数据库连接
    mydb = mysql.connector.connect(
        host="gz-cynosdbmysql-grp-5dm79phb.sql.tencentcdb.com",
        port=27114,
        user="ghostlee",
        password="Ly200210",
        auth_plugin="mysql_native_password",
        database="data"
        # host="127.0.0.1",
        # port=3306,
        # user="root",
        # password="Fys8211200417",
        # database="shitai_db"
    )

    # 创建游标对象
    mycursor = mydb.cursor()

    # 执行SQL查询
    try:
        for index, row in info.iterrows():
            update="INSERT INTO `"+str(target_scene)+"-features-projection`"+ \
                   " (source,batch,time_min,time_max,duration,min_lon,max_lon,min_lat,max_lat,avg_vel,avg_accel,avg_cou,"+ \
                   "avg_anguvel,points,sparsity,projection_x_ISOMAP,projection_y_ISOMAP,projection_x_KPCA,projection_y_KPCA,"+ \
                   "projection_x_MDS,projection_y_MDS,projection_x_PCA,projection_y_PCA,"+ \
                   "projection_x_TSNE,projection_y_TSNE,projection_x_UMAP,projection_y_UMAP,projection_x_LDA,projection_y_LDA) VALUES("+ \
                   str(row['source'])+","+str(row['batch'])+","+str(row['time_min'])+","+str(row['time_max'])+","+str(row['duration'])+","+ \
                   str(row['min_lon'])+","+str(row['max_lon'])+","+str(row['min_lat'])+","+str(row['max_lat'])+","+str(row['avg_vel'])+","+ \
                   str(row['avg_accel'])+","+str(row['avg_cou'])+","+str(row['avg_anguvel'])+","+str(row['points'])+","+ \
                   str(row['sparsity'])+","+str(row['projection_x_ISOMAP'])+","+str(row['projection_y_ISOMAP'])+","+ \
                   str(row['projection_x_KPCA'])+","+str(row['projection_y_KPCA'])+","+ \
                   str(row['projection_x_MDS'])+","+str(row['projection_y_MDS'])+","+ \
                   str(row['projection_x_PCA'])+","+str(row['projection_y_PCA'])+","+ \
                   str(row['projection_x_TSNE'])+","+str(row['projection_y_TSNE'])+","+ \
                   str(row['projection_x_UMAP'])+","+str(row['projection_y_UMAP'])+","+ \
                   str(row['projection_x_LDA'])+","+str(row['projection_y_LDA'])+")"
            mycursor.execute(update)
            mydb.commit()
            # if index%10==0 and index!=0:
            #   print("finish "+str(index))
    except:
        print("Error!")
    mydb.rollback()
    mycursor.close()
    mydb.close()

def isomap(X, n_components=2, n_neighbors=5):
    """
    :param X: 高维数据，shape为(n_samples, n_features)
    :param n_components: 降维后的特征数，默认为2
    :param n_neighbors: 用于计算k近邻的邻居数，默认为5
    :return: 降维后的数据，shape为(n_samples, n_components)
    """
    # 计算距离矩阵
    nn = NearestNeighbors(n_neighbors=n_neighbors + 1)
    nn.fit(X)
    distances, indices = nn.kneighbors(X)
    distances = distances[:, 1:]
    indices = indices[:, 1:]
    # 构建邻接矩阵
    adj_matrix = np.zeros((X.shape[0], X.shape[0]))
    for i in range(X.shape[0]):
        adj_matrix[i, indices[i]] = distances[i]
        adj_matrix[indices[i], i] = distances[i]
    # 计算最短路径
    D = Isomap(n_neighbors=n_neighbors, n_components=n_components).fit_transform(X)
    return D

def rbf_kernel(X, Y, gamma):
    XX = np.sum(X ** 2, axis=-1)
    YY = np.sum(Y ** 2, axis=-1)
    XY = np.dot(X, Y.T)
    K = np.exp(-gamma * (XX[:, None] + YY[None, :] - 2 * XY))
    return K

def mds(X, n_components=2):
    """
    :param X: 高维数据，shape为(n_samples, n_features)
    :param n_components: 降维后的特征数，默认为2
    :return: 降维后的数据，shape为(n_samples, n_components)
    """
    # 计算距离矩阵
    distances = pairwise_distances(X, metric='euclidean')
    # 计算Gram矩阵
    G = -0.5 * np.square(distances)
    # 双中心化
    n = G.shape[0]
    H = np.eye(n) - np.ones((n, n)) / n
    B = np.dot(np.dot(H, G), H)
    # 特征值分解
    eigenvalues, eigenvectors = np.linalg.eigh(B)
    # 选取前n_components大的特征值对应的特征向量
    idx = np.argsort(-eigenvalues)[:n_components]
    eigenvectors = eigenvectors[:, idx]
    # 降维
    D = np.dot(eigenvectors, np.diag(np.sqrt(eigenvalues[idx])))
    return D

def umap_reduction(X, n_neighbors=15, min_dist=0.1, n_components=2):
    """
    UMAP降维算法
    :param X: 原始数据集
    :param n_neighbors: 邻居数
    :param min_dist: 最小距离
    :param n_components: 降维后的维度
    :return: 归一化后的降维结果
    """
    umap_obj = umap.UMAP(n_neighbors=n_neighbors, min_dist=min_dist, n_components=n_components)
    X_umap = umap_obj.fit_transform(X)
    scaler = MinMaxScaler()
    X_umap_norm = scaler.fit_transform(X_umap)
    return X_umap_norm

def lda(XMat,y):
    lda = LinearDiscriminantAnalysis(n_components=2)
    X_lda = lda.fit_transform(XMat,y)
    result = (X_lda - np.min(X_lda, axis=0)) / (np.max(X_lda, axis=0) - np.min(X_lda, axis=0))
    return result

def partIsomap(XMat,df):
   D = isomap(XMat, n_components=2, n_neighbors=5)
   result = (D - np.min(D, axis=0)) / (np.max(D, axis=0) - np.min(D, axis=0))
   res_df = pd.DataFrame(data=result[0:, 0:], columns=['projection_x_ISOMAP', 'projection_y_ISOMAP'])
   df = df.join(res_df, how='outer')
   #print("ISOMAP DONE!")
   for row in range(0,len(df)):
       print(str(int(df.iloc[row][0]))+','+str(int(df.iloc[row][1]))+','+str(int(df.iloc[row][2]))+','+str(df.iloc[row][3])+','+str(df.iloc[row][4])+','+str(df.iloc[row][5])+','+str(df.iloc[row][6])+','+str(df.iloc[row][7])+','+str(df.iloc[row][8])+','+str(df.iloc[row][9])+','+str(df.iloc[row][10])+','+str(df.iloc[row][11])+','+str(df.iloc[row][12])+','+str(df.iloc[row][13])+','+str(df.iloc[row][14])+','+str(df.iloc[row][15])+','+str(df.iloc[row][16])+','+str(df.iloc[row][17]))


def partKPCA(XMat,df):
    # KPCA
    kpca = KernelPCA(n_components=2, kernel='precomputed')
    # 计算核矩阵
    K = rbf_kernel(XMat.values, XMat.values, gamma=5)
    # 训练KPCA模型
    X_kpca = kpca.fit_transform(K)
    result = (X_kpca - np.min(X_kpca, axis=0)) / (np.max(X_kpca, axis=0) - np.min(X_kpca, axis=0))
    res_df = pd.DataFrame(data=result[0:, 0:], columns=['projection_x_KPCA', 'projection_y_KPCA'])
    df = df.join(res_df, how='outer')
    #print("KPCA DONE!")
    for row in range(0,len(df)):
        print(str(int(df.iloc[row][0]))+','+str(int(df.iloc[row][1]))+','+str(int(df.iloc[row][2]))+','+str(df.iloc[row][3])+','+str(df.iloc[row][4])+','+str(df.iloc[row][5])+','+str(df.iloc[row][6])+','+str(df.iloc[row][7])+','+str(df.iloc[row][8])+','+str(df.iloc[row][9])+','+str(df.iloc[row][10])+','+str(df.iloc[row][11])+','+str(df.iloc[row][12])+','+str(df.iloc[row][13])+','+str(df.iloc[row][14])+','+str(df.iloc[row][15])+','+str(df.iloc[row][16])+','+str(df.iloc[row][17]))


def partMDS(XMat,df):
    #MDS
    D = mds(XMat, n_components=2)
    result = (D - np.min(D, axis=0)) / (np.max(D, axis=0) - np.min(D, axis=0))
    res_df = pd.DataFrame(data=result[0:, 0:], columns=['projection_x_MDS', 'projection_y_MDS'])
    df = df.join(res_df, how='outer')
    #print("MDS DONE!")
    for row in range(0,len(df)):
        print(str(int(df.iloc[row][0]))+','+str(int(df.iloc[row][1]))+','+str(int(df.iloc[row][2]))+','+str(df.iloc[row][3])+','+str(df.iloc[row][4])+','+str(df.iloc[row][5])+','+str(df.iloc[row][6])+','+str(df.iloc[row][7])+','+str(df.iloc[row][8])+','+str(df.iloc[row][9])+','+str(df.iloc[row][10])+','+str(df.iloc[row][11])+','+str(df.iloc[row][12])+','+str(df.iloc[row][13])+','+str(df.iloc[row][14])+','+str(df.iloc[row][15])+','+str(df.iloc[row][16])+','+str(df.iloc[row][17]))


def partPCA(XMat,df):
    #PCA
    pca = PCA(n_components=2)
    pca.fit(XMat)
    res = pca.transform(XMat)
    result = (res - np.min(res, axis=0)) / (np.max(res, axis=0) - np.min(res, axis=0))
    res_df = pd.DataFrame(data=result[0:, 0:], columns=['projection_x_PCA', 'projection_y_PCA'])
    df = df.join(res_df, how='outer')
    #print("PCA DONE!")
    for row in range(0,len(df)):
        print(str(int(df.iloc[row][0]))+','+str(int(df.iloc[row][1]))+','+str(int(df.iloc[row][2]))+','+str(df.iloc[row][3])+','+str(df.iloc[row][4])+','+str(df.iloc[row][5])+','+str(df.iloc[row][6])+','+str(df.iloc[row][7])+','+str(df.iloc[row][8])+','+str(df.iloc[row][9])+','+str(df.iloc[row][10])+','+str(df.iloc[row][11])+','+str(df.iloc[row][12])+','+str(df.iloc[row][13])+','+str(df.iloc[row][14])+','+str(df.iloc[row][15])+','+str(df.iloc[row][16])+','+str(df.iloc[row][17]))


def partTSNE(XMat,df):
    #TSNE
    # 创建t-SNE模型，将数据降到二维
    tsne = TSNE(n_components=2, random_state=15)
    data_tsne = tsne.fit_transform(XMat.values)
    result = (data_tsne - np.min(data_tsne, axis=0)) / (np.max(data_tsne, axis=0) - np.min(data_tsne, axis=0))
    res_df = pd.DataFrame(data=result[0:, 0:], columns=['projection_x_TSNE', 'projection_y_TSNE'])
    df = df.join(res_df, how='outer')
    #print("TSNE DONE!")
    for row in range(0,len(df)):
        print(str(int(df.iloc[row][0]))+','+str(int(df.iloc[row][1]))+','+str(int(df.iloc[row][2]))+','+str(df.iloc[row][3])+','+str(df.iloc[row][4])+','+str(df.iloc[row][5])+','+str(df.iloc[row][6])+','+str(df.iloc[row][7])+','+str(df.iloc[row][8])+','+str(df.iloc[row][9])+','+str(df.iloc[row][10])+','+str(df.iloc[row][11])+','+str(df.iloc[row][12])+','+str(df.iloc[row][13])+','+str(df.iloc[row][14])+','+str(df.iloc[row][15])+','+str(df.iloc[row][16])+','+str(df.iloc[row][17]))


def partUMAP(XMat,df):
    #UMAP
    D=umap_reduction(XMat.to_numpy())
    result = (D - np.min(D, axis=0)) / (np.max(D, axis=0) - np.min(D, axis=0))
    res_df = pd.DataFrame(data=result[0:, 0:], columns=['projection_x_UMAP', 'projection_y_UMAP'])
    df = df.join(res_df, how='outer')
    #print("UMAP DONE!")
    for row in range(0,len(df)):
        print(str(int(df.iloc[row][0]))+','+str(int(df.iloc[row][1]))+','+str(int(df.iloc[row][2]))+','+str(df.iloc[row][3])+','+str(df.iloc[row][4])+','+str(df.iloc[row][5])+','+str(df.iloc[row][6])+','+str(df.iloc[row][7])+','+str(df.iloc[row][8])+','+str(df.iloc[row][9])+','+str(df.iloc[row][10])+','+str(df.iloc[row][11])+','+str(df.iloc[row][12])+','+str(df.iloc[row][13])+','+str(df.iloc[row][14])+','+str(df.iloc[row][15])+','+str(df.iloc[row][16])+','+str(df.iloc[row][17]))


def partLDA(XMat,df):
    #LDA
    L=lda(XMat.to_numpy(),df["batch"])
    result = (L - np.min(L, axis=0)) / (np.max(L, axis=0) - np.min(L, axis=0))
    res_df = pd.DataFrame(data=result[0:, 0:], columns=['projection_x_LDA', 'projection_y_LDA'])
    df = df.join(res_df, how='outer')
    #print("LDA DONE!")
    pd.set_option('display.max_rows', None)
    for row in range(0,len(df)):
        print(str(int(df.iloc[row][0]))+','+str(int(df.iloc[row][1]))+','+str(int(df.iloc[row][2]))+','+str(df.iloc[row][3])+','+str(df.iloc[row][4])+','+str(df.iloc[row][5])+','+str(df.iloc[row][6])+','+str(df.iloc[row][7])+','+str(df.iloc[row][8])+','+str(df.iloc[row][9])+','+str(df.iloc[row][10])+','+str(df.iloc[row][11])+','+str(df.iloc[row][12])+','+str(df.iloc[row][13])+','+str(df.iloc[row][14])+','+str(df.iloc[row][15])+','+str(df.iloc[row][16])+','+str(df.iloc[row][17]))


# 读取数据
# scene_index = 3223
scene_index = int(sys.argv[1])  # 定义场景文件序号
model = sys.argv[2]
fList = pd.read_csv(io.StringIO(sys.argv[3]), lineterminator=',',header=None)
#for item in fList:
#    print(fList)
# X = pd.read_csv(f'../data/data_{scene_index}/场景-{scene_index}-features-normalized.csv', sep=',')
df = getDate(scene_index)
XMat = df.loc[:,fList[0]]
#print(XMat)
#print("Begin!")

if model == "isomap":
    partIsomap(XMat,df)
elif model == "kpca":
    partKPCA(XMat,df)
elif model == "mds":
    partMDS(XMat,df)
elif model == "pca":
    partPCA(XMat,df)
elif model == "tsne":
    partTSNE(XMat,df)
elif model == "umap":
    partLDA(XMat,df)
elif model == "lda":
    partLDA(XMat,df)
else:
    print("目标表输入错误")


#saveDate(df,scene_index)


#df.to_csv(f'../data/data_{scene_index}/场景-{scene_index}-projection-isomap.csv', sep=',', index=False, header=True)
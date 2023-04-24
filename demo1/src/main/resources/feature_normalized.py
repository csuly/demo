# -*- coding: UTF-8 -*-
"""
@author: Shirley 
@time: 2023/4/12 18:36
@project: mtad_dataProcess
@file: feature_normalized.py
@desc: 特征数据归一化
"""
import mysql.connector
import pandas as pd

def createTable(target_scene):
    # 建立数据库连接
    mydb = mysql.connector.connect(
    host="localhost",
    user="root",
    password="Fys8211200417",
    auth_plugin="mysql_native_password",
    database="shitai_db")
    # 创建游标对象
    mycursor = mydb.cursor()
    # 执行SQL查询
    mycursor.execute("DROP TABLE IF EXISTS `"+str(target_scene)+"-features-normalized`;")
    mycursor.execute(
    "CREATE TABLE `"+str(target_scene)+"-features-normalized` ("+\
    "id int NOT NULL AUTO_INCREMENT,"+"source int,"+"batch int,"+"time_min float,"+"time_max float,"+\
    "duration float,"+"min_lon float,"+"max_lon float,"+"min_lat float,"+\
    "max_lat float,"+"avg_vel float,"+"avg_accel float,"+"avg_cou float,"+\
    "avg_anguvel float,"+"points float,"+"sparsity float,"+\
    "PRIMARY KEY (id) );"
    )

def getDate(target_scene):
  # 建立数据库连接
  mydb = mysql.connector.connect(
    host="localhost",
    user="root",
    password="Fys8211200417",
    auth_plugin="mysql_native_password",
    database="shitai_db"
  )

  # 创建游标对象
  mycursor = mydb.cursor()

  # 执行SQL查询
  mycursor.execute(
    "SELECT id,source,batch,time_min,time_max,duration,min_lon,max_lon,min_lat,max_lat,avg_vel,avg_accel,avg_cou,avg_anguvel,points,sparsity"
    +" FROM `"+str(target_scene)+"-features`")

  # 获取查询结果
  myresult = mycursor.fetchall()
  result=pd.DataFrame(list(myresult))
  result.columns=['id','source','batch','time_min','time_max','duration','min_lon','max_lon','min_lat','max_lat','avg_vel','avg_accel','avg_cou','avg_anguvel','points','sparsity']
  # 打印查询结果
  #print(result)
  mycursor.close()
  mydb.close()
  return result

def saveDate(info,target_scene):
 # 建立数据库连接
  mydb = mysql.connector.connect(
    host="localhost",
    user="root",
    password="Fys8211200417",
    auth_plugin="mysql_native_password",
    database="shitai_db"
  )

  # 创建游标对象
  mycursor = mydb.cursor()
  
  # 执行SQL查询
  try:
    for index, row in info.iterrows():
      update="INSERT INTO `"+str(target_scene)+"-features-normalized`"+\
      " (source,batch,time_min,time_max,duration,min_lon,max_lon,min_lat,max_lat,avg_vel,avg_accel,avg_cou,"+\
      "avg_anguvel,points,sparsity) VALUES("+\
      str(row['source'])+","+str(row['batch'])+","+str(row['time_min'])+","+str(row['time_max'])+","+str(row['duration'])+","+\
      str(row['min_lon'])+","+str(row['max_lon'])+","+str(row['min_lat'])+","+str(row['max_lat'])+","+str(row['avg_vel'])+","+\
      str(row['avg_accel'])+","+str(row['avg_cou'])+","+str(row['avg_anguvel'])+","+str(row['points'])+","+\
      str(row['sparsity'])+")"
      mycursor.execute(update)
      mydb.commit()
      #if index%10==0 and index!=0:
        #print("finish "+str(index))
  except:
    print("Error!")
  mydb.rollback()
  mycursor.close()
  mydb.close()

# 一、准备工作
scene_list=["1054","2802","3223"]
for scene_index in scene_list:
    createTable(scene_index)
    # 1. 读取场景航迹特征数据
    csv_df = getDate(scene_index)
    # 2.获取所有特征名称
    columns = csv_df.columns.tolist()
    columns.remove("source")
    columns.remove("batch")
    # 3.对每个特征数据进行归一化处理
    for i, item in enumerate(columns):
        csv_df[item] = (csv_df[item] - csv_df[item].min()) / (csv_df[item].max() - csv_df[item].min())
    # 4.将归一化数据写入文件
    saveDate(csv_df,scene_index)
    print(str(scene_index)+" Done!")

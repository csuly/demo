# -*- coding: UTF-8 -*-
"""
@author: Shirley 
@time: 2023/4/12 17:15
@project: mtad_dataProcess
@file: feature_model.py
@desc: 航迹数据特征建模
"""

import mysql.connector
import pandas as pd
import numpy as np
import sys


# 函数
# 读取数据文件
def read_file(path):
    csv_data = pd.read_csv(path, low_memory=False)
    csv_df = pd.DataFrame(csv_data)
    return csv_df


# 各信源航迹批号统计
def batch_count(source):
    # 筛选出该批号下所有航迹数据点
    source_data = csv_df.loc[csv_df['source'] == source]
    # 统计得到该信源下所有批号的集合
    source_batch = source_data['batch'].unique().tolist()  # 统计batch属性列中所有可能值，即batch集合
    return source_batch


# 创建信源字典
def source_dic(source_batch, source, dic):
    for i, b in enumerate(source_batch):
        # 筛选出该批号下所有航迹数据点
        track = csv_df.query(f'source=={source} & batch=={b}')
        track = track.reset_index(drop=True)
        # 向字典中存入一条信息
        dic[b] = track


# 以下，source_batch表示批号集合——key，dic表示批号及数据点集合字典——map
# 1. 时间特征
# 1.1 起止时间统计
def time_stats(source_batch, dic):
    time_se = {}
    time_min = []
    time_max = []
    for i, b in enumerate(source_batch):
        # 统计每个批号中的航迹起止时间
        track = dic[b]
        time_s = track['time'].min()
        time_min.append(time_s)

        time_e = track['time'].max()
        time_max.append(time_e)

    time_se['time_min'] = time_min
    time_se['time_max'] = time_max
    return time_se


# 1.2 轨迹时长统计
def duration_stats(source_batch, dic):
    duration = []
    for i, b in enumerate(source_batch):
        # 统计每个批号中的航迹持续时间
        track = dic[b]
        time_s = track['time'].min()
        time_e = track['time'].max()
        duration.append(time_e - time_s)

    return duration


# 2. 位置特征
# 2.1 最小/最大经纬度统计
def lat_and_lon_stats(source_batch, dic):
    lat_and_lon = {}
    lat_1 = []
    lat_2 = []
    lon_1 = []
    lon_2 = []
    for i, b in enumerate(source_batch):
        # 统计每个批号中的航迹持续时间
        track = dic[b]
        lat_min = track['lat'].min()
        lat_1.append(lat_min)

        lat_max = track['lat'].max()
        lat_2.append(lat_max)

        lon_min = track['lon'].min()
        lon_1.append(lon_min)

        lon_max = track['lon'].max()
        lon_2.append(lon_max)

    lat_and_lon['lat_min'] = lat_1
    lat_and_lon['lat_max'] = lat_2
    lat_and_lon['lon_min'] = lon_1
    lat_and_lon['lon_max'] = lon_2
    return lat_and_lon


# 3. 速度特征
# 3.1 平均航速统计
def avg_vel_stats(source_batch, dic):
    avg_vel = []
    for i, b in enumerate(source_batch):
        # 统计每个批号中的航迹持续时间
        track = dic[b]
        vel = track['vel'].mean()
        vel = format(vel, '.6f')
        avg_vel.append(vel)
    return avg_vel


# 3.2 加速度统计
def avg_acceleration_stats(source_batch, dic):
    avg_accel = []
    for i, b in enumerate(source_batch):
        # 统计每个批号中的航迹持续时间
        track = dic[b].reset_index(drop=True)  # 当前批号航迹数据点集合，同时重置索引
        accel_sum = 0
        pre_item = {}
        for index, item in track.iterrows():
            if index == 0:
                accel_sum = 0
                pre_item = item
                continue

            if (item['time'] - pre_item['time']) != 0:
                accel_sum = accel_sum + (item['vel'] - pre_item['vel']) / (item['time'] - pre_item['time'])
            pre_item = item
        # 计算加速度平均值
        avg_accel.append(format((accel_sum / track.shape[0]), '.6f'))
    return avg_accel


# 4. 方向特征
# 4.1 平均航向统计
def avg_cou_stats(source_batch, dic):
    avg_cou = []
    for i, b in enumerate(source_batch):
        # 统计每个批号中的航迹持续时间
        track = dic[b]
        cou = track['cou'].mean()
        cou = format(cou, '.6f')
        avg_cou.append(cou)

    return avg_cou


# 4.2 转向角速度统计
def avg_angularvel_stats(source_batch, dic):
    avg_angu = []
    for i, b in enumerate(source_batch):
        # 统计每个批号中的航迹持续时间
        track = dic[b].reset_index(drop=True)  # 当前批号航迹数据点集合，同时重置索引
        angu_sum = 0
        pre_item = {}
        for index, item in track.iterrows():
            if index == 0:
                angu_sum = 0
                pre_item = item
                continue

            if (item['lon'] - pre_item['lon']) != 0:
                angu_sum = angu_sum + np.arctan((item['lat'] - pre_item['lat']) / (item['lon'] - pre_item['lon']))

            pre_item = item

        # 计算加速度平均值
        avg_angu.append(format((angu_sum / track.shape[0]), '.6f'))

    return avg_angu


# 5. 其他特征
# 5.1 数据点数量
def points_stats(source_batch, dic):
    points = []
    for i, b in enumerate(source_batch):
        # 统计每个批号的数据点数量
        track = dic[b]
        ps = track.shape[0]
        points.append(ps)
    return points


# 5.2 采样率
def sparsity_stats(source_batch, dic):
    sparsity = []
    for i, b in enumerate(source_batch):
        # 统计每个批号的数据点数量
        track = dic[b]
        points = track.shape[0]
        time_s = track['time'].min()
        time_e = track['time'].max()
        time = time_e - time_s
        if time == 0:
            ss = 0
        else:
            ss = format((time / points), '.6f')
        sparsity.append(ss)
    return sparsity


# 场景3223处理
def scene_3223():
    # 一、数据准备
    # 1.统计每个信源下批号集合
    source_9001_batch = batch_count(9001)
    source_9002_batch = batch_count(9002)

    # 2.将每个信源下的所有批号及其对应数据点集合存为一个字典
    # 2.1 9001
    dic_9001 = {}  # 创建一个空字典
    source_dic(source_9001_batch, 9001, dic_9001)

    # 2.2 9002
    dic_9002 = {}  # 创建一个空字典
    source_dic(source_9002_batch, 9002, dic_9002)

    # 二、特征值计算
    # 创建两个dataframe，用于存储计算下来的特征值
    feature_df_9001 = pd.DataFrame(index=source_9001_batch)
    feature_df_9002 = pd.DataFrame(index=source_9002_batch)

    feature_df_9001['source'] = 9001
    feature_df_9001['batch'] = source_9001_batch

    feature_df_9002['source'] = 9002
    feature_df_9002['batch'] = source_9002_batch

    # 特征统计
    # 1. 时间特征
    # 1.1 统计起止时间
    tt_9001 = time_stats(source_9001_batch, dic_9001)
    feature_df_9001['time_min'] = tt_9001['time_min']
    feature_df_9001['time_max'] = tt_9001['time_max']
    tt_9002 = time_stats(source_9002_batch, dic_9002)
    feature_df_9002['time_min'] = tt_9002['time_min']
    feature_df_9002['time_max'] = tt_9002['time_max']

    # 1.2 统计持续时间
    feature_df_9001['duration'] = duration_stats(source_9001_batch, dic_9001)
    feature_df_9002['duration'] = duration_stats(source_9002_batch, dic_9002)

    # 2. 位置特征
    # 2.1 统计经纬度范围
    ll_9001 = lat_and_lon_stats(source_9001_batch, dic_9001)
    feature_df_9001['min_lon'] = ll_9001['lon_min']
    feature_df_9001['max_lon'] = ll_9001['lon_max']
    feature_df_9001['min_lat'] = ll_9001['lat_min']
    feature_df_9001['max_lat'] = ll_9001['lat_max']
    ll_9002 = lat_and_lon_stats(source_9002_batch, dic_9002)
    feature_df_9002['min_lon'] = ll_9002['lon_min']
    feature_df_9002['max_lon'] = ll_9002['lon_max']
    feature_df_9002['min_lat'] = ll_9002['lat_min']
    feature_df_9002['max_lat'] = ll_9002['lat_max']

    # 3. 航速特征
    # 3.1 统计平均航速
    feature_df_9001['avg_vel'] = avg_vel_stats(source_9001_batch, dic_9001)
    feature_df_9002['avg_vel'] = avg_vel_stats(source_9002_batch, dic_9002)

    # 3.2 统计平均加速度
    feature_df_9001['avg_accel'] = avg_acceleration_stats(source_9001_batch, dic_9001)
    feature_df_9002['avg_accel'] = avg_acceleration_stats(source_9002_batch, dic_9002)

    # 4. 航向特征
    # 4.1 统计平均航向
    feature_df_9001['avg_cou'] = avg_cou_stats(source_9001_batch, dic_9001)
    feature_df_9002['avg_cou'] = avg_cou_stats(source_9002_batch, dic_9002)

    # 4.2 统计平均转向角速度
    feature_df_9001['avg_anguvel'] = avg_angularvel_stats(source_9001_batch, dic_9001)
    feature_df_9002['avg_anguvel'] = avg_angularvel_stats(source_9002_batch, dic_9002)

    # 5. 其他
    # 5.1 数据点数量
    feature_df_9001['points'] = points_stats(source_9001_batch, dic_9001)
    feature_df_9002['points'] = points_stats(source_9002_batch, dic_9002)

    # 5.2 采样率
    feature_df_9001['sparsity'] = sparsity_stats(source_9001_batch, dic_9001)
    feature_df_9002['sparsity'] = sparsity_stats(source_9002_batch, dic_9002)

    # 三、特征文件导出
    df = feature_df_9001.append(feature_df_9002)
    df.sort_values(by=['source', 'batch'], inplace=True, ascending=True)
    print(df)
    return df


# 场景2802处理
def scene_2802():
    # 一、数据准备
    # 1.统计每个信源下批号集合
    source_9001_batch = batch_count(9001)
    source_9002_batch = batch_count(9002)
    source_9003_batch = batch_count(9003)

    # 2.将每个信源下的所有批号及其对应数据点集合存为一个字典
    # 2.1 9001
    dic_9001 = {}  # 创建一个空字典
    source_dic(source_9001_batch, 9001, dic_9001)

    # 2.2 9002
    dic_9002 = {}  # 创建一个空字典
    source_dic(source_9002_batch, 9002, dic_9002)

    # 2.3 9003
    dic_9003 = {}  # 创建一个空字典
    source_dic(source_9003_batch, 9003, dic_9003)

    # 二、特征值计算
    # 创建两个dataframe，用于存储计算下来的特征值
    feature_df_9001 = pd.DataFrame(index=source_9001_batch)
    feature_df_9002 = pd.DataFrame(index=source_9002_batch)
    feature_df_9003 = pd.DataFrame(index=source_9003_batch)

    feature_df_9001['source'] = 9001
    feature_df_9001['batch'] = source_9001_batch

    feature_df_9002['source'] = 9002
    feature_df_9002['batch'] = source_9002_batch

    feature_df_9003['source'] = 9003
    feature_df_9003['batch'] = source_9003_batch

    # 特征统计
    # 1. 时间特征
    # 1.1 统计起止时间
    tt_9001 = time_stats(source_9001_batch, dic_9001)
    feature_df_9001['time_min'] = tt_9001['time_min']
    feature_df_9001['time_max'] = tt_9001['time_max']
    tt_9002 = time_stats(source_9002_batch, dic_9002)
    feature_df_9002['time_min'] = tt_9002['time_min']
    feature_df_9002['time_max'] = tt_9002['time_max']
    tt_9003 = time_stats(source_9003_batch, dic_9003)
    feature_df_9003['time_min'] = tt_9003['time_min']
    feature_df_9003['time_max'] = tt_9003['time_max']

    # 1.2 统计持续时间
    feature_df_9001['duration'] = duration_stats(source_9001_batch, dic_9001)
    feature_df_9002['duration'] = duration_stats(source_9002_batch, dic_9002)
    feature_df_9003['duration'] = duration_stats(source_9003_batch, dic_9003)

    # 2. 位置特征
    # 2.1 统计经纬度范围
    ll_9001 = lat_and_lon_stats(source_9001_batch, dic_9001)
    feature_df_9001['min_lon'] = ll_9001['lon_min']
    feature_df_9001['max_lon'] = ll_9001['lon_max']
    feature_df_9001['min_lat'] = ll_9001['lat_min']
    feature_df_9001['max_lat'] = ll_9001['lat_max']
    ll_9002 = lat_and_lon_stats(source_9002_batch, dic_9002)
    feature_df_9002['min_lon'] = ll_9002['lon_min']
    feature_df_9002['max_lon'] = ll_9002['lon_max']
    feature_df_9002['min_lat'] = ll_9002['lat_min']
    feature_df_9002['max_lat'] = ll_9002['lat_max']
    ll_9003 = lat_and_lon_stats(source_9003_batch, dic_9003)
    feature_df_9003['min_lon'] = ll_9003['lon_min']
    feature_df_9003['max_lon'] = ll_9003['lon_max']
    feature_df_9003['min_lat'] = ll_9003['lat_min']
    feature_df_9003['max_lat'] = ll_9003['lat_max']

    # 3. 航速特征
    # 3.1 统计平均航速
    feature_df_9001['avg_vel'] = avg_vel_stats(source_9001_batch, dic_9001)
    feature_df_9002['avg_vel'] = avg_vel_stats(source_9002_batch, dic_9002)
    feature_df_9003['avg_vel'] = avg_vel_stats(source_9003_batch, dic_9003)

    # 3.2 统计平均加速度
    feature_df_9001['avg_accel'] = avg_acceleration_stats(source_9001_batch, dic_9001)
    feature_df_9002['avg_accel'] = avg_acceleration_stats(source_9002_batch, dic_9002)
    feature_df_9003['avg_accel'] = avg_acceleration_stats(source_9003_batch, dic_9003)

    # 4. 航向特征
    # 4.1 统计平均航向
    feature_df_9001['avg_cou'] = avg_cou_stats(source_9001_batch, dic_9001)
    feature_df_9002['avg_cou'] = avg_cou_stats(source_9002_batch, dic_9002)
    feature_df_9003['avg_cou'] = avg_cou_stats(source_9003_batch, dic_9003)

    # 4.2 统计平均转向角速度
    feature_df_9001['avg_anguvel'] = avg_angularvel_stats(source_9001_batch, dic_9001)
    feature_df_9002['avg_anguvel'] = avg_angularvel_stats(source_9002_batch, dic_9002)
    feature_df_9003['avg_anguvel'] = avg_angularvel_stats(source_9003_batch, dic_9003)

    # 5. 其他
    # 5.1 数据点数量
    feature_df_9001['points'] = points_stats(source_9001_batch, dic_9001)
    feature_df_9002['points'] = points_stats(source_9002_batch, dic_9002)
    feature_df_9003['points'] = points_stats(source_9003_batch, dic_9003)

    # 5.2 采样率
    feature_df_9001['sparsity'] = sparsity_stats(source_9001_batch, dic_9001)
    feature_df_9002['sparsity'] = sparsity_stats(source_9002_batch, dic_9002)
    feature_df_9003['sparsity'] = sparsity_stats(source_9003_batch, dic_9003)

    # 三、特征文件导出
    df = feature_df_9001.append(feature_df_9002).append(feature_df_9003)
    df.sort_values(by=['source', 'batch'], inplace=True, ascending=True)
    return df


# 场景1054处理
def scene_1054():
    # 一、数据准备
    # 1.统计每个信源下批号集合
    source_9001_batch = batch_count(9001)
    source_9002_batch = batch_count(9002)
    source_9003_batch = batch_count(9003)
    source_9004_batch = batch_count(9004)
    source_9005_batch = batch_count(9005)

    # 2.将每个信源下的所有批号及其对应数据点集合存为一个字典
    # 2.1 9001
    dic_9001 = {}  # 创建一个空字典
    source_dic(source_9001_batch, 9001, dic_9001)

    # 2.2 9002
    dic_9002 = {}  # 创建一个空字典
    source_dic(source_9002_batch, 9002, dic_9002)

    # 2.3 9003
    dic_9003 = {}  # 创建一个空字典
    source_dic(source_9003_batch, 9003, dic_9003)

    # 2.4 9004
    dic_9004 = {}  # 创建一个空字典
    source_dic(source_9004_batch, 9004, dic_9004)

    # 2.5 9005
    dic_9005 = {}  # 创建一个空字典
    source_dic(source_9005_batch, 9005, dic_9005)

    # 二、特征值计算
    # 创建两个dataframe，用于存储计算下来的特征值
    feature_df_9001 = pd.DataFrame(index=source_9001_batch)
    feature_df_9002 = pd.DataFrame(index=source_9002_batch)
    feature_df_9003 = pd.DataFrame(index=source_9003_batch)
    feature_df_9004 = pd.DataFrame(index=source_9004_batch)
    feature_df_9005 = pd.DataFrame(index=source_9005_batch)

    feature_df_9001['source'] = 9001
    feature_df_9001['batch'] = source_9001_batch

    feature_df_9002['source'] = 9002
    feature_df_9002['batch'] = source_9002_batch

    feature_df_9003['source'] = 9003
    feature_df_9003['batch'] = source_9003_batch

    feature_df_9004['source'] = 9004
    feature_df_9004['batch'] = source_9004_batch

    feature_df_9005['source'] = 9005
    feature_df_9005['batch'] = source_9005_batch

    # 特征统计
    # 1. 时间特征
    # 1.1 统计起止时间
    tt_9001 = time_stats(source_9001_batch, dic_9001)
    feature_df_9001['time_min'] = tt_9001['time_min']
    feature_df_9001['time_max'] = tt_9001['time_max']
    tt_9002 = time_stats(source_9002_batch, dic_9002)
    feature_df_9002['time_min'] = tt_9002['time_min']
    feature_df_9002['time_max'] = tt_9002['time_max']
    tt_9003 = time_stats(source_9003_batch, dic_9003)
    feature_df_9003['time_min'] = tt_9003['time_min']
    feature_df_9003['time_max'] = tt_9003['time_max']
    tt_9004 = time_stats(source_9004_batch, dic_9004)
    feature_df_9004['time_min'] = tt_9004['time_min']
    feature_df_9004['time_max'] = tt_9004['time_max']
    tt_9005 = time_stats(source_9005_batch, dic_9005)
    feature_df_9005['time_min'] = tt_9005['time_min']
    feature_df_9005['time_max'] = tt_9005['time_max']

    # 1.2 统计持续时间
    feature_df_9001['duration'] = duration_stats(source_9001_batch, dic_9001)
    feature_df_9002['duration'] = duration_stats(source_9002_batch, dic_9002)
    feature_df_9003['duration'] = duration_stats(source_9003_batch, dic_9003)
    feature_df_9004['duration'] = duration_stats(source_9004_batch, dic_9004)
    feature_df_9005['duration'] = duration_stats(source_9005_batch, dic_9005)

    # 2. 位置特征
    # 2.1 统计经纬度范围
    ll_9001 = lat_and_lon_stats(source_9001_batch, dic_9001)
    feature_df_9001['min_lon'] = ll_9001['lon_min']
    feature_df_9001['max_lon'] = ll_9001['lon_max']
    feature_df_9001['min_lat'] = ll_9001['lat_min']
    feature_df_9001['max_lat'] = ll_9001['lat_max']
    ll_9002 = lat_and_lon_stats(source_9002_batch, dic_9002)
    feature_df_9002['min_lon'] = ll_9002['lon_min']
    feature_df_9002['max_lon'] = ll_9002['lon_max']
    feature_df_9002['min_lat'] = ll_9002['lat_min']
    feature_df_9002['max_lat'] = ll_9002['lat_max']
    ll_9003 = lat_and_lon_stats(source_9003_batch, dic_9003)
    feature_df_9003['min_lon'] = ll_9003['lon_min']
    feature_df_9003['max_lon'] = ll_9003['lon_max']
    feature_df_9003['min_lat'] = ll_9003['lat_min']
    feature_df_9003['max_lat'] = ll_9003['lat_max']
    ll_9004 = lat_and_lon_stats(source_9004_batch, dic_9004)
    feature_df_9004['min_lon'] = ll_9004['lon_min']
    feature_df_9004['max_lon'] = ll_9004['lon_max']
    feature_df_9004['min_lat'] = ll_9004['lat_min']
    feature_df_9004['max_lat'] = ll_9004['lat_max']
    ll_9005 = lat_and_lon_stats(source_9005_batch, dic_9005)
    feature_df_9005['min_lon'] = ll_9005['lon_min']
    feature_df_9005['max_lon'] = ll_9005['lon_max']
    feature_df_9005['min_lat'] = ll_9005['lat_min']
    feature_df_9005['max_lat'] = ll_9005['lat_max']

    # 3. 航速特征
    # 3.1 统计平均航速
    feature_df_9001['avg_vel'] = avg_vel_stats(source_9001_batch, dic_9001)
    feature_df_9002['avg_vel'] = avg_vel_stats(source_9002_batch, dic_9002)
    feature_df_9003['avg_vel'] = avg_vel_stats(source_9003_batch, dic_9003)
    feature_df_9004['avg_vel'] = avg_vel_stats(source_9004_batch, dic_9004)
    feature_df_9005['avg_vel'] = avg_vel_stats(source_9005_batch, dic_9005)

    # 3.2 统计平均加速度
    feature_df_9001['avg_accel'] = avg_acceleration_stats(source_9001_batch, dic_9001)
    feature_df_9002['avg_accel'] = avg_acceleration_stats(source_9002_batch, dic_9002)
    feature_df_9003['avg_accel'] = avg_acceleration_stats(source_9003_batch, dic_9003)
    feature_df_9004['avg_accel'] = avg_acceleration_stats(source_9004_batch, dic_9004)
    feature_df_9005['avg_accel'] = avg_acceleration_stats(source_9005_batch, dic_9005)

    # 4. 航向特征
    # 4.1 统计平均航向
    feature_df_9001['avg_cou'] = avg_cou_stats(source_9001_batch, dic_9001)
    feature_df_9002['avg_cou'] = avg_cou_stats(source_9002_batch, dic_9002)
    feature_df_9003['avg_cou'] = avg_cou_stats(source_9003_batch, dic_9003)
    feature_df_9004['avg_cou'] = avg_cou_stats(source_9004_batch, dic_9004)
    feature_df_9005['avg_cou'] = avg_cou_stats(source_9005_batch, dic_9005)

    # 4.2 统计平均转向角速度
    feature_df_9001['avg_anguvel'] = avg_angularvel_stats(source_9001_batch, dic_9001)
    feature_df_9002['avg_anguvel'] = avg_angularvel_stats(source_9002_batch, dic_9002)
    feature_df_9003['avg_anguvel'] = avg_angularvel_stats(source_9003_batch, dic_9003)
    feature_df_9004['avg_anguvel'] = avg_angularvel_stats(source_9004_batch, dic_9004)
    feature_df_9005['avg_anguvel'] = avg_angularvel_stats(source_9005_batch, dic_9005)

    # 5. 其他
    # 5.1 数据点数量
    feature_df_9001['points'] = points_stats(source_9001_batch, dic_9001)
    feature_df_9002['points'] = points_stats(source_9002_batch, dic_9002)
    feature_df_9003['points'] = points_stats(source_9003_batch, dic_9003)
    feature_df_9004['points'] = points_stats(source_9004_batch, dic_9004)
    feature_df_9005['points'] = points_stats(source_9005_batch, dic_9005)

    # 5.2 采样率
    feature_df_9001['sparsity'] = sparsity_stats(source_9001_batch, dic_9001)
    feature_df_9002['sparsity'] = sparsity_stats(source_9002_batch, dic_9002)
    feature_df_9003['sparsity'] = sparsity_stats(source_9003_batch, dic_9003)
    feature_df_9004['sparsity'] = sparsity_stats(source_9004_batch, dic_9004)
    feature_df_9005['sparsity'] = sparsity_stats(source_9005_batch, dic_9005)

    # 三、特征数据整理
    df = feature_df_9001.append(feature_df_9002).append(feature_df_9003).append(feature_df_9004).append(feature_df_9005)
    df.sort_values(by=['source', 'batch'], inplace=True, ascending=True)
    return df


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
    "SELECT id,batch,source,time,lon,lat,vel,cou"
    +" FROM `"+str(target_scene)+"-points`")

  # 获取查询结果
  myresult = mycursor.fetchall()
  result=pd.DataFrame(list(myresult))
  result.columns=['id','batch','source','time','lon','lat','vel','cou']
  # 打印查询结果
  print(result)
  mycursor.close()
  mydb.close()
  return result

#将数据存储到数据库
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
    i=0
    for index, row in info.iterrows():
      update="UPDATE `"+str(target_scene)+"-features` SET "+\
            "time_min="+str(row['time_min'])+\
            ", time_max="+str(row['time_max'])+\
            ", duration="+str(row['duration'])+\
            ", min_lon="+str(row['min_lon'])+\
            ", max_lon="+str(row['max_lon'])+\
            ", min_lat="+str(row['min_lat'])+\
            ", max_lat="+str(row['max_lat'])+\
            ", avg_vel="+str(row['avg_vel'])+\
            ", avg_accel="+str(row['avg_accel'])+\
            ", avg_cou="+str(row['avg_cou'])+\
            ", avg_anguvel="+str(row['avg_anguvel'])+\
            ", points="+str(row['points'])+\
            ", sparsity="+str(row['sparsity'])+\
            " WHERE source="+str(row['source'])+\
            " AND batch="+str(row['batch'])
      mycursor.execute(update)
      mydb.commit()
      if index%10==0 and index!=0:
        print("finish "+str(index))
    print("Done!")
  except:
    print("Error!")
  mydb.rollback()
  mycursor.close()
  mydb.close()

"""
统计的特征值包括：
1.时间属性：开始时间偏移量、结束时间偏移量、持续时间
2.位置属性：最大最小经纬度
3.航速属性：平均航速、平均加速度
4.航向属性：平均航向、平均转向角速度
5.其他：数据点个数、采样率
"""

scene_index = int(sys.argv[1])  # 定义场景文件序号
#scene_index=3223

# 1.读取场景航迹数据
#csv_df = read_file(f"../data/data_{scene_index}/场景-{scene_index}-points.csv")
csv_df=getDate(scene_index)
print(csv_df)

# 2.调用对应计算方法
df = pd.DataFrame()

if scene_index == 3223:
    df = scene_3223()
elif scene_index == 2802:
    df = scene_2802()
elif scene_index == 1054:
    df = scene_1054()

saveDate(df,scene_index)
#df.to_csv(f'../data/data_{scene_index}/场景-{scene_index}-features.csv', encoding='utf-8', index=False, header=True)

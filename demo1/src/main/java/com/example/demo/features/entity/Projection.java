package com.example.demo.features.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Projection {
    private int id;
    private int source;
    private int batch;
    private float time_min;
    private float time_max;
    private float duration;
    private float min_lon;
    private float max_lon;
    private float min_lat;
    private float max_lat;
    private float avg_vel;
    private float avg_accel;
    private float avg_cou;
    private float avg_anguvel;
    private float points;
    private float sparsity;
    private float projection_x_ISOMAP;
    private float projection_y_ISOMAP;
    private float projection_x_KPCA;
    private float projection_y_KPCA;
    private float projection_x_MDS;
    private float projection_y_MDS;
    private float projection_x_PCA;
    private float projection_y_PCA;
    private float projection_x_TSNE;
    private float projection_y_TSNE;
    private float projection_x_UMAP;
    private float projection_y_UMAP;
}

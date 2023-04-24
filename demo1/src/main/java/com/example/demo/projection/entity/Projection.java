package com.example.demo.projection.entity;

import lombok.*;
import org.hibernate.mapping.Map;

import javax.persistence.*;
import java.util.HashMap;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Projection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "source")
    private int source;
    @Column(name = "batch")
    private int batch;
    @Column(name = "time_min")
    private double time_min;
    @Column(name = "time_max")
    private double time_max;
    @Column(name = "duration")
    private double duration;
    @Column(name = "min_lon")
    private double min_lon;
    @Column(name = "max_lon")
    private double max_lon;
    @Column(name = "min_lat")
    private double min_lat;
    @Column(name = "max_lat")
    private double max_lat;
    @Column(name = "avg_vel")
    private double avg_vel;
    @Column(name = "avg_accel")
    private double avg_accel;
    @Column(name = "avg_cou")
    private double avg_cou;
    @Column(name = "avg_anguvel")
    private double avg_anguvel;
    @Column(name = "points")
    private int points;
    @Column(name = "sparsity")
    private double sparsity;
    @Column(name="projection_x_ISOMAP")
    private double projection_x_ISOMAP;
    @Column(name = "projection_y_ISOMAP")
    private double projection_y_ISOMAP;
    @Column(name="projection_x_KPCA")
    private double projection_x_KPCA;
    @Column(name="projection_y_KPCA")
    private double projection_y_KPCA;
    @Column(name="projection_x_MDS")
    private double projection_x_MDS;
    @Column(name="projection_y_MDS")
    private double projection_y_MDS;
    @Column(name="projection_x_PCA")
    private double projection_x_PCA;
    @Column(name="projection_y_PCA")
    private double projection_y_PCA;
    @Column(name="projection_x_TSNE")
    private double projection_x_TSNE;
    @Column(name="projection_y_TSNE")
    private double projection_y_TSNE;
    @Column(name="projection_x_UMAP")
    private double projection_x_UMAP;
    @Column(name="projection_y_UMAP")
    private double projection_y_UMAP;

}

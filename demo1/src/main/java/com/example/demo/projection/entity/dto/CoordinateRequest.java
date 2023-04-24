package com.example.demo.projection.entity.dto;

import com.example.demo.projection.entity.Projection;

import javax.persistence.Column;

public record CoordinateRequest(
        double projection_x_ISOMAP,
        double projection_y_ISOMAP,
        double projection_x_KPCA,
        double projection_y_KPCA,
        double projection_x_MDS,
        double projection_y_MDS,
        double projection_x_PCA,
        double  projection_y_PCA,
        double projection_x_TSNE,
        double projection_y_TSNE,
        double projection_x_UMAP,
        double projection_y_UMAP,
        boolean highlight,
        Projection data
) {}


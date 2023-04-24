package com.example.demo.projection.controller;

import com.example.demo.Result;
import com.example.demo.projection.entity.Projection;
import com.example.demo.projection.entity.Projection1054;
import com.example.demo.projection.entity.Projection2802;
import com.example.demo.projection.entity.Projection3223;
import com.example.demo.projection.entity.dto.CoordinateRequest;
import com.example.demo.projection.entity.dto.QueryItem;
import com.example.demo.projection.entity.dto.QueryRequest;
import com.example.demo.projection.repository.Projection1054Repository;
import com.example.demo.projection.repository.Projection2802Repository;
import com.example.demo.projection.repository.Projection3223Repository;
import com.example.demo.projection.repository.ProjectionRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProjectionController {
    private final Projection1054Repository projection1054Repository;
    private final Projection2802Repository projection2802Repository;
    private final Projection3223Repository projection3223Repository;
    private final Map<Integer, ProjectionRepository> repositoryMap = new HashMap<>();

    public ProjectionController(Projection1054Repository projection1054Repository, Projection2802Repository projection2802Repository, Projection3223Repository projection3223Repository) {
        this.projection1054Repository = projection1054Repository;
        this.projection2802Repository = projection2802Repository;
        this.projection3223Repository = projection3223Repository;
        repositoryMap.put(2,projection3223Repository);
        repositoryMap.put(3,projection2802Repository);
        repositoryMap.put(5,projection1054Repository);
    }

    @PostMapping("/getProjection")
    public Result projection(@RequestBody QueryRequest request)
    {
        if(!request.validate())
            return Result.error("400","Invalid request");
        List<CoordinateRequest> data = new ArrayList<>();
        ProjectionRepository repository = repositoryMap.get(request.getSourceSize());
        if(repository == null)
            return Result.error("400","No such table");
        for(List<QueryItem> itemList:request.getSources())
        {
            List<Projection> features = new ArrayList<>();
            for(QueryItem item : itemList)
            {
                if(item.batch()==-1)
                    continue;
                List<Projection> res = repository.findAllByBatchAndSource(item.batch(), item.source());
                features.add(res.get(0));
                for(Projection f : res)
                {
                    data.add(new CoordinateRequest(f.getProjection_x_ISOMAP(), f.getProjection_y_ISOMAP(),
                            f.getProjection_x_KPCA(),f.getProjection_y_KPCA(),
                            f.getProjection_x_MDS(),f.getProjection_y_MDS(),
                            f.getProjection_x_PCA(),f.getProjection_y_PCA(),
                            f.getProjection_x_TSNE(),f.getProjection_y_TSNE(),
                            f.getProjection_x_UMAP(),f.getProjection_y_UMAP(),
                            true,f));
                }
            }
        }
        if(request.getSourceSize() == 3)
        {
            List<Projection2802> features = projection2802Repository.findAll();
            for(Projection f : features)
            {
                if(!data.contains(new CoordinateRequest(f.getProjection_x_ISOMAP(), f.getProjection_y_ISOMAP(),
                    f.getProjection_x_KPCA(),f.getProjection_y_KPCA(),
                    f.getProjection_x_MDS(),f.getProjection_y_MDS(),
                    f.getProjection_x_PCA(),f.getProjection_y_PCA(),
                    f.getProjection_x_TSNE(),f.getProjection_y_TSNE(),
                    f.getProjection_x_UMAP(),f.getProjection_y_UMAP(),
                    true,f)))
                    data.add(new CoordinateRequest(f.getProjection_x_ISOMAP(), f.getProjection_y_ISOMAP(),
                            f.getProjection_x_KPCA(),f.getProjection_y_KPCA(),
                            f.getProjection_x_MDS(),f.getProjection_y_MDS(),
                            f.getProjection_x_PCA(),f.getProjection_y_PCA(),
                            f.getProjection_x_TSNE(),f.getProjection_y_TSNE(),
                            f.getProjection_x_UMAP(),f.getProjection_y_UMAP(),
                            true,f));
            }
        } else if (request.getSourceSize() == 2) {
            List<Projection3223> features = projection3223Repository.findAll();
            for(Projection f : features)
            {
                if(!data.contains(new CoordinateRequest(f.getProjection_x_ISOMAP(), f.getProjection_y_ISOMAP(),
                        f.getProjection_x_KPCA(),f.getProjection_y_KPCA(),
                        f.getProjection_x_MDS(),f.getProjection_y_MDS(),
                        f.getProjection_x_PCA(),f.getProjection_y_PCA(),
                        f.getProjection_x_TSNE(),f.getProjection_y_TSNE(),
                        f.getProjection_x_UMAP(),f.getProjection_y_UMAP(),
                        true,f)))
                    data.add(new CoordinateRequest(f.getProjection_x_ISOMAP(), f.getProjection_y_ISOMAP(),
                            f.getProjection_x_KPCA(),f.getProjection_y_KPCA(),
                            f.getProjection_x_MDS(),f.getProjection_y_MDS(),
                            f.getProjection_x_PCA(),f.getProjection_y_PCA(),
                            f.getProjection_x_TSNE(),f.getProjection_y_TSNE(),
                            f.getProjection_x_UMAP(),f.getProjection_y_UMAP(),
                            true,f));
            }
        } else if (request.getSourceSize()==5) {
            List<Projection1054> features = projection1054Repository.findAll();
            for(Projection f : features)
            {
                if(!data.contains(new CoordinateRequest(f.getProjection_x_ISOMAP(), f.getProjection_y_ISOMAP(),
                        f.getProjection_x_KPCA(),f.getProjection_y_KPCA(),
                        f.getProjection_x_MDS(),f.getProjection_y_MDS(),
                        f.getProjection_x_PCA(),f.getProjection_y_PCA(),
                        f.getProjection_x_TSNE(),f.getProjection_y_TSNE(),
                        f.getProjection_x_UMAP(),f.getProjection_y_UMAP(),
                        true,f)))
                    data.add(new CoordinateRequest(f.getProjection_x_ISOMAP(), f.getProjection_y_ISOMAP(),
                            f.getProjection_x_KPCA(),f.getProjection_y_KPCA(),
                            f.getProjection_x_MDS(),f.getProjection_y_MDS(),
                            f.getProjection_x_PCA(),f.getProjection_y_PCA(),
                            f.getProjection_x_TSNE(),f.getProjection_y_TSNE(),
                            f.getProjection_x_UMAP(),f.getProjection_y_UMAP(),
                            true,f));
            }
        }
        return Result.success(data);
    }
}

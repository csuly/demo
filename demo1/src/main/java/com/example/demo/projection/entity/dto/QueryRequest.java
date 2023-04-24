package com.example.demo.projection.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QueryRequest {

    private int sourceSize;

    private int page;

    private int pageSize;

    private List<List<QueryItem>> sources;

    public boolean validate() {
        for (List<QueryItem> stringList : sources) {
            if (stringList.size() != sourceSize)
                return false;
        }
        return true;
    }
}

package com.example.uberapp_tim22.DTO;

import com.example.uberapp_tim22.model.User;

import java.util.ArrayList;
import java.util.List;

public class AllPanicsDTO {
    private Integer totalCount;
    private List<PanicDTO> allPanics = null;

    public AllPanicsDTO(int totalCount, ArrayList<PanicDTO> results) {
        this.totalCount = totalCount;
        this.allPanics = results;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<PanicDTO> getallPanics() {
        return allPanics;
    }

    public void setallPanics(List<PanicDTO> results) {
        this.allPanics = results;
    }

}

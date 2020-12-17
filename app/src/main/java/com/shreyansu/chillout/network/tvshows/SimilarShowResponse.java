package com.shreyansu.chillout.network.tvshows;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SimilarShowResponse
{
    @SerializedName("page")
    private Integer page;
    @SerializedName("total_pages")
    private Integer totalPages;
    @SerializedName("results")
    private List<ShowDetail> results;
    @SerializedName("total_results")
    private Integer total_result;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<ShowDetail> getResults() {
        return results;
    }

    public void setResults(List<ShowDetail> results) {
        this.results = results;
    }

    public Integer getTotal_result() {
        return total_result;
    }

    public void setTotal_result(Integer total_result) {
        this.total_result = total_result;
    }
}

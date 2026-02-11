package com.idea.ad.search;

import com.idea.ad.search.vo.SearchRequest;
import com.idea.ad.search.vo.SearchResponse;

public interface ISearch {
    SearchResponse fetchAds(SearchRequest request);
}

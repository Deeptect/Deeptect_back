package com.deeptactback.deeptact_back.service;

import com.google.api.services.youtube.model.SearchResult;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface YoutubeShortsFetchService {
    List<String> fetchAndStoreYoutubeShorts() throws IOException, GeneralSecurityException;
    List<SearchResult> searchShorts() throws IOException, GeneralSecurityException;
    String streamVideoDirectlyToR2(String videoId, String title) throws IOException, InterruptedException;
}

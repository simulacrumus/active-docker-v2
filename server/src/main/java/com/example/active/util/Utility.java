package com.example.active.util;

import org.apache.commons.text.similarity.LevenshteinDistance;

public class Utility {
    public static final int MAX_STRING_EDIT_DISTANCE = 3;
    public static int stringDistanceScore(String query, String title){
        query = query.toLowerCase();
        title = title.toLowerCase();
        if(query.equals("")) return 0;
        if(query.equals(title)) return 0;
        if(title.startsWith(query)) return 0;
        int distanceForQueryLength = Integer.MAX_VALUE;
        if(query.length() <= title.length()){
            distanceForQueryLength = LevenshteinDistance.getDefaultInstance().apply(title.substring(0, query.length() - 1), query);
        }
        String[] words = title.split("\\s+");
        int minDistance = Integer.MAX_VALUE;
        for(String word : words){
            if(word.startsWith(query)) return 0;
            int distance = LevenshteinDistance.getDefaultInstance().apply(word, query);
            minDistance = Math.min(distance, minDistance);
        }
        return Math.min(distanceForQueryLength, minDistance);
    }
}

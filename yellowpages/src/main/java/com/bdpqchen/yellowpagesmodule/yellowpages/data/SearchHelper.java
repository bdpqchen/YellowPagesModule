package com.bdpqchen.yellowpagesmodule.yellowpages.data;

/**
 * Copyright (C) 2015 Ari C.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;

import com.bdpqchen.yellowpagesmodule.yellowpages.model.SearchResult;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.WordSuggestion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchHelper {

    private static final String COLORS_FILE_NAME = "colors.json";

//    private static List<ColorWrapper> sColorWrappers = new ArrayList<>();

    // TODO: 17-2-26 it should be replaced with search results and new ... to fit the format.
    private static List<WordSuggestion> sWordSuggestions =
            new ArrayList<>(Arrays.asList(
                    new WordSuggestion("green"),
                    new WordSuggestion("blue"),
                    new WordSuggestion("pink"),
                    new WordSuggestion("purple"),
                    new WordSuggestion("brown"),
                    new WordSuggestion("gray"),
                    new WordSuggestion("Granny Smith Apple"),
                    new WordSuggestion("Indigo"),
                    new WordSuggestion("Periwinkle"),
                    new WordSuggestion("Mahogany"),
                    new WordSuggestion("Maize"),
                    new WordSuggestion("Mahogany"),
                    new WordSuggestion("Outer Space"),
                    new WordSuggestion("Melon"),
                    new WordSuggestion("Yellow"),
                    new WordSuggestion("Orange"),
                    new WordSuggestion("Red"),
                    new WordSuggestion("Orchid")));

    public interface OnFindWordListener {
        void onResults(List<SearchResult> results);
    }

    public static void findWord(Context context, String query, final OnFindWordListener listener) {
        // TODO: 17-2-25 find data list by query
        List<SearchResult> resultList = new ArrayList<>();

        SearchResult results = new SearchResult();
        for (int i = 0; i < 5; i++) {
            results.isStow = i < 2;
            results.name = "name" + i;
            results.phone = String.valueOf(i * 100000);
            resultList.add(results);
        }
        if (listener != null) {
            listener.onResults(resultList);
        }

    }

    public static List<WordSuggestion> getHistory(Context context, int count) {
        List<WordSuggestion> suggestionList = new ArrayList<>();
        WordSuggestion colorSuggestion;
        for (int i = 0; i < sWordSuggestions.size(); i++) {
            colorSuggestion = sWordSuggestions.get(i);
            colorSuggestion.setIsHistory(true);
            suggestionList.add(colorSuggestion);
            if (suggestionList.size() == count) {
                break;
            }
        }
        return suggestionList;
    }

    public interface OnFindSuggestionsListener {
        void onResults(List<WordSuggestion> results);
    }

    public static void findSuggestions(Context context, String query, final int limit, final OnFindSuggestionsListener listener) {
        // TODO: 17-2-26 on a new thread to query the key`s results data list, and return to mainThread.
        // TODO: 17-2-26 the query must to match cases: 1.no result, 2 so many data, 3...

        // to query ,query(query, limit, and so on.)
        List<SearchResult> resultList = new ArrayList<>();
        for (int i = 0; i < limit + 1; i++) {
            SearchResult results = new SearchResult();
            results.isStow = i < 2;
            results.phone = String.valueOf(i * 100000);
            results.name = "name" + i + "\n" + results.phone;

            /*//如果结果比较多的话，提示用户发起检索
            if (i == limit){
                results.name = "点此查看全部结果";
                results.phone = query;
            }*/

            resultList.add(results);
        }

        List<WordSuggestion> wordSuggestions = new ArrayList<>();
        for (int i = 0; i < resultList.size(); i++){
            WordSuggestion wordSuggestion = new WordSuggestion(resultList.get(i).name);
            wordSuggestions.add(wordSuggestion);
        }
        if (listener != null) {
            listener.onResults(wordSuggestions);
        }
    }

}
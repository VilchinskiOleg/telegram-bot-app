package com.resliv.bot.service;

import com.resliv.bot.model.QuizDataItem;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.TreeMap;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.groupingBy;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "quiz-data")
public class QuizDataService {

    private List<QuizDataItem> items;
    private TreeMap<String, List<QuizDataItem>> itemsByQuestion;

    @PostConstruct
    public void initQuizDataItem() {
        itemsByQuestion = new TreeMap<>(items.stream().collect(groupingBy(QuizDataItem::getQuestion)));
    }

    public List<QuizDataItem> pullItems(String question) {
        return itemsByQuestion.remove(question);
    }

    public String getNextQuestion() {
        return itemsByQuestion.firstKey();
    }
}
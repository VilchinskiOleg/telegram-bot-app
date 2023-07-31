package com.resliv.bot.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class QuizDataItem {

    private String question;
    private String answer;
    private Boolean right;
}
package com.example.quiz.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*クイズテーブル用entity*/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {
	//識別id
	@Id
	private Integer id;

	//クイズの内容
	private String question;

	//クイズの回答
	private String answer;
	
	//不正解選択肢変数
	private String select_one;
	
	private String select_two;
	
	private String select_three;
	
	// 問題難易度のラベルカラー
	private String color;


}

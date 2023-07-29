package com.example.quiz.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizForm {
	//識別id
	private Integer id;

	//クイズの内容
	@NotBlank
	private String question;

	//クイズの回答
	@NotBlank
	private String answer;
	
	
	
	//不正解選択肢変数
	@NotBlank
	private String select_one;
	
	@NotBlank
	private String select_two;
	
	@NotBlank
	private String select_three;
	
	@NotBlank
	private String color;



	//登録or変更
	private Boolean newQuiz;
	
	
}

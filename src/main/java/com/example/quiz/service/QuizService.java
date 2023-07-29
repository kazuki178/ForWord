package com.example.quiz.service;

import java.util.Optional;

import com.example.quiz.entity.Quiz;

/*quizサービス処理*/
public interface QuizService {

	// 単語情報を全件取得
	Iterable<Quiz> selectAll();

	// クイズ情報をidキーに1件取得
	Optional<Quiz> selectOneById(Integer id);

	// 単語情報をランダムで1件取得
	Optional<Quiz> selectOneRandomQuiz();

	//　赤色のidを全権取得
	Optional<Quiz> selectOneRedRandomQuiz();

	//　黄色のidを全権取得
	Optional<Quiz> selectOneYellowRandomQuiz();

	//　緑色のidを全権取得
	Optional<Quiz> selectOneGreenRandomQuiz();

	// レコード数を取得
	long count();

	// 単語の正解不正解を判定
	Boolean checkQuiz(Integer id, String ansewer);

	// 単語を登録
	void insertQuiz(Quiz quiz);

	// 単語を更新
	void updateQuiz(Quiz quiz);

	// 単語を削除
	void deleteQuizById(Integer id);

}

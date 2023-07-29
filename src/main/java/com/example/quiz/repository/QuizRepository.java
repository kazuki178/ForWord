package com.example.quiz.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.quiz.entity.Quiz;

public interface QuizRepository extends CrudRepository<Quiz, Integer> {

	@Query("SELECT id FROM quiz ORDER BY RANDOM() limit 1")
	Integer getRamdomId();

	// 難易度'赤'のidを取得する
	@Query("SELECT id FROM quiz where color ='赤' ORDER BY RANDOM() limit 1;")
	Integer getRandomRedId();

	// 難易度'黄'のidを取得する
	@Query("SELECT id FROM quiz where color ='黄' ORDER BY RANDOM() limit 1;")
	Integer getRandomYellowId();

	// 難易度'緑'のidを取得する
	@Query("SELECT id FROM quiz where color ='緑' ORDER BY RANDOM() limit 1;")
	Integer getRandomGreenId();
}

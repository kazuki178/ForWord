package com.example.quiz.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.quiz.entity.Quiz;
import com.example.quiz.repository.QuizRepository;

@Transactional
@Service
public class QuizServiceImpl implements QuizService {

	/*repository:注入*/
	@Autowired
	QuizRepository repository;

	@Override
	public Iterable<Quiz> selectAll() {
		return repository.findAll();
	}

	@Override
	public Optional<Quiz> selectOneById(Integer id) {
		return repository.findById(id);
	}

	@Override
	public Optional<Quiz> selectOneRandomQuiz() {
		//ランダムでidの値を取得する
		Integer randId = repository.getRamdomId();

		//問題がない場合
		if (randId == null) {
			//空のoptionalインスタンスを返す
			return Optional.empty();
		}

		return repository.findById(randId);
	}

	// 赤色のidを取得
	@Override
	public Optional<Quiz> selectOneRedRandomQuiz() {
		//ランダムでidの値を取得する
		Integer randRedId = repository.getRandomRedId();

		//問題がない場合
		if (randRedId == null) {
			//空のoptionalインスタンスを返す
			return Optional.empty();
		}

		return repository.findById(randRedId);
	}

	// 黄色のidを取得
	@Override
	public Optional<Quiz> selectOneYellowRandomQuiz() {
		//ランダムでidの値を取得する
		Integer randYellowId = repository.getRandomYellowId();

		//問題がない場合
		if (randYellowId == null) {
			//空のoptionalインスタンスを返す
			return Optional.empty();
		}

		return repository.findById(randYellowId);
	}

	// 緑色のidを取得
	@Override
	public Optional<Quiz> selectOneGreenRandomQuiz() {
		//ランダムでidの値を取得する
		Integer randGreenId = repository.getRandomGreenId();

		//問題がない場合
		if (randGreenId == null) {
			//空のoptionalインスタンスを返す
			return Optional.empty();
		}

		return repository.findById(randGreenId);
	}

	@Override
	public Boolean checkQuiz(Integer id, String answer) {
		//単語正解不正解判定用変数
		Boolean check = false;

		//対象の単語を取得
		Optional<Quiz> optQuiz = repository.findById(id);

		Quiz quiz = optQuiz.get();

		String quizAnswer = quiz.getAnswer();

		//クイズの回答チェック
		if (quizAnswer.equals(answer)) {
			check = true;
		}

		return check;
	}

	@Override
	public long count() {
		return repository.count();
	}

	@Override
	public void insertQuiz(Quiz quiz) {
		repository.save(quiz);

	}

	@Override
	public void updateQuiz(Quiz quiz) {
		repository.save(quiz);

	}

	@Override
	public void deleteQuizById(Integer id) {
		repository.deleteById(id);
	}

}

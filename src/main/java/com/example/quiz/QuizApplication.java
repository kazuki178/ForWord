package com.example.quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.quiz.entity.Quiz;
import com.example.quiz.service.QuizService;

@SpringBootApplication
public class QuizApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizApplication.class, args);//.getBean(QuizApplication.class).execute();
	}

	//	注入
	@Autowired
	QuizService service;

	//実行メソッド 
	private void execute() {
		//登録
		//setup();

		//全件取得
		//showList();

		//一件取得
		//	showOne();

		//更新処理
		//updateQuiz();

		//削除処理
		//deleteQuiz();

		//クイズを実行する
		//doQuiz();

	}

	//クイズ登録
	private void setup() {
		//エンティティの生成
		Quiz quiz1 = new Quiz(null, "refreshments", "軽食", "朝食", "夕食", "夜食", "");
		Quiz quiz2 = new Quiz(null, "warehouse", "倉庫", "車庫", "押し入れ", "寝床", "");
		Quiz quiz3 = new Quiz(null, "available", "利用できる", "利用できない", "到着", "飛び立つ", "");
		Quiz quiz4 = new Quiz(null, "inventory", "倉庫", "車庫", "歴史", "本棚", "");
		Quiz quiz5 = new Quiz(null, "reserve", "～を予約する", "口論する", "購入する", "売却する", "");

		//リストにentityを格納
		List<Quiz> quizList = new ArrayList<>();

		//第一引数に格納先、第二引数に可変長引数なのでentityを記述
		Collections.addAll(quizList, quiz1, quiz2, quiz3, quiz4, quiz5);

		for (Quiz quiz : quizList) {
			//登録実行
			service.insertQuiz(quiz);
		}

		//確認出力
		System.out.println("ーーーーー登録処理完了ーーーーー");

	}

	//全件取得
	private void showList() {
		System.out.println("ーーーーー全件取得開始ーーーーー");

		//リポジトリを使用して全件取得開始
		Iterable<Quiz> quizzes = service.selectAll();
		for (Quiz quiz : quizzes) {
			System.out.println(quiz);
		}
		System.out.println("ーーーーー全件取得完了ーーーーー");
	}

	//１件表示
	private void showOne() {
		System.out.println("ーーーーー1件取得開始ーーーーー");

		//findByIdは戻り値がoptional
		Optional<Quiz> quizOpt = service.selectOneById(1);

		//optionalはnullを許容するため存在チェック
		if (quizOpt.isPresent()) {
			System.out.println(quizOpt.get());
		} else {
			System.out.println("検討する問題がありません");
		}

		System.out.println("ーーーーー１件取得完了ーーーーー");
	}

	//更新処理
	private void updateQuiz() {
		System.out.println("ーーーーー更新処理開始ーーーーー");

		//更新したいentityを生成する
		Quiz quiz1 = new Quiz(1, "budget", "予算", "型掛けバッグ", "ジェット", "飛行機","");

		//更新実行
		service.updateQuiz(quiz1);

		//更新確認
		System.out.println("ーーーーー更新処理完了ーーーーー");
	}

	//削除処理
	private void deleteQuiz() {
		System.out.println("ーーーーー削除処理開始ーーーーー");

		//削除処理実行
		service.deleteQuizById(2);

		//削除完了通知
		System.out.println("ーーーーー削除完了ーーーーー");
	}

}

package com.example.quiz.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.quiz.entity.Quiz;
import com.example.quiz.form.QuizForm;
import com.example.quiz.service.QuizService;

/*quizコントローラ*/
@Controller
@RequestMapping("/quiz")
public class QuizController {

	/*DI対象*/
	@Autowired
	QuizService service;

	/*form-backing beanの初期化*/
	@ModelAttribute
	public QuizForm setUpForm() {
		QuizForm form = new QuizForm();

		//ラジオボタンのデフォルト値設定

		return form;
	}

	//quizの一覧を表示します
	@GetMapping("/crud")
	public String showListcrud(QuizForm quizForm, Model model) {
		//新規登録設定用
		quizForm.setNewQuiz(true);
		quizForm.setColor("未設定");

		//掲示板の一覧を取得する
		Iterable<Quiz> list = service.selectAll();

		//表示用modelへ格納
		model.addAttribute("list", list);
		model.addAttribute("title", "新規単語登録");

		return "crud";
	}

	@GetMapping
	public String showList(QuizForm quizForm, Model model) {
		//新規登録設定用
		quizForm.setNewQuiz(true);

		//掲示板の一覧を取得する
		Iterable<Quiz> list = service.selectAll();

		//表示用modelへ格納
		model.addAttribute("list", list);
		model.addAttribute("title", "ホーム");

		return "index";
	}

	//quizの一覧を表示します
	public String RedirectshowList(QuizForm quizForm, Model model, RedirectAttributes redirectAttributes) {
		//新規登録設定用	
		quizForm.setNewQuiz(true);

		//掲示板の一覧を取得する
		Iterable<Quiz> list = service.selectAll();

		//表示用modelへ格納
		redirectAttributes.addFlashAttribute("list", list);
		redirectAttributes.addFlashAttribute("title", "新規単語登録画面");
		redirectAttributes.addFlashAttribute("notcomplete", "空白は許可されていません");

		return "redirect:/quiz/crud";
	}

	/** Quizデータを1件挿入 */
	@PostMapping("/insert")
	public String insert(@Validated QuizForm quizForm, BindingResult bindingResult,
			Model model, RedirectAttributes redirectAttributes) {

		// データベース処理をするためにFormからEntityへの詰め替え
		Quiz quiz = new Quiz();
		quiz.setQuestion(quizForm.getQuestion());
		quiz.setAnswer(quizForm.getAnswer());
		quiz.setSelect_one(quizForm.getSelect_one());
		quiz.setSelect_two(quizForm.getSelect_two());
		quiz.setSelect_three(quizForm.getSelect_three());
		quiz.setColor(quizForm.getColor());

		// 入力チェック
		if (!bindingResult.hasErrors()) {
			service.insertQuiz(quiz);
			// リダイレクト先でのみ有効
			redirectAttributes.addFlashAttribute("complete", "登録が完了しました");
			return "redirect:/quiz/crud";
		} else {
			// エラーがある場合は、上の一覧表示処理を呼びます
			return RedirectshowList(quizForm, model, redirectAttributes);
		}
	}

	/**urlにidの値を埋め込むリクエストパラメータ*/
	/** Quizデータを１件取得し、フォーム内に表示する */
	@GetMapping("/{id}")
	public String showUpdate(QuizForm quizForm, @PathVariable Integer id, Model model) {
		//Quizを取得(Optionalでラップ)
		Optional<Quiz> quizOpt = service.selectOneById(id);
		//QuizFormへの詰め直し
		Optional<QuizForm> quizFormOpt = quizOpt.map(t -> makeQuizForm(t));
		//QuizFormがnullでなければ中身を取り出す
		if (quizFormOpt.isPresent()) {
			quizForm = quizFormOpt.get();
		}
		// 更新用のModelを作成する
		makeUpdateModel(quizForm, model);
		return "crud";
	}

	/** 更新用のModelを作成する */
	private void makeUpdateModel(QuizForm quizForm, Model model) {
		model.addAttribute("id", quizForm.getId());
		quizForm.setNewQuiz(false);
		model.addAttribute("quizForm", quizForm);
		model.addAttribute("title", "単語更新画面");
	}

	/** idをKeyにしてデータを更新する */
	@PostMapping("/update")
	public String update(
			@Validated QuizForm quizForm,
			BindingResult result,
			Model model,
			RedirectAttributes redirectAttributes) {
		//QuizFormからQuizに詰め直す
		Quiz quiz = makeQuiz(quizForm);
		// 入力チェック
		if (!result.hasErrors()) {
			//更新処理、フラッシュスコープの使用、リダイレクト（個々の編集ページ）
			service.updateQuiz(quiz);
			redirectAttributes.addFlashAttribute("complete", "更新が完了しました");
			// 更新画面を表示する
			return "redirect:/quiz/" + quiz.getId();
		} else {
			// 更新用のModelを作成する
			makeUpdateModel(quizForm, model);
			return "crud";
		}
	}

	// ---------- 【以下はFormとDomainObjectの詰めなおし】 ----------
	/** QuizFormからQuizに詰め直して戻り値とし返します */
	private Quiz makeQuiz(QuizForm quizForm) {
		Quiz quiz = new Quiz();
		quiz.setId(quizForm.getId());
		quiz.setQuestion(quizForm.getQuestion());
		quiz.setAnswer(quizForm.getAnswer());
		quiz.setSelect_one(quizForm.getSelect_one());
		quiz.setSelect_two(quizForm.getSelect_two());
		quiz.setSelect_three(quizForm.getSelect_three());
		quiz.setColor(quizForm.getColor());

		return quiz;
	}

	/** QuizからQuizFormに詰め直して戻り値とし返します */
	private QuizForm makeQuizForm(Quiz quiz) {
		QuizForm form = new QuizForm();
		form.setId(quiz.getId());
		form.setQuestion(quiz.getQuestion());
		form.setAnswer(quiz.getAnswer());
		form.setSelect_one(quiz.getSelect_one());
		form.setSelect_two(quiz.getSelect_two());
		form.setSelect_three(quiz.getSelect_three());
		form.setColor(quiz.getColor());

		form.setNewQuiz(false);
		return form;
	}

	/*idをキーにしてデータを削除する*/
	@PostMapping("/delete")
	public String delete(@RequestParam("id") String id, Model model, RedirectAttributes redirectattribute) {
		//タスクを１件削除してリダイレクト
		service.deleteQuizById(Integer.parseInt(id));
		redirectattribute.addFlashAttribute("delcomplete", "削除が完了しました");
		return "redirect:/quiz";

	}

	/*単語で学習機能*/
	@PostMapping("/play")
	public String showQuiz(@RequestParam(name = "BeforeId", required = false) Integer BeforeId, QuizForm quizForm,
			QuizForm quizFormSelect1, QuizForm quizFormSelect2, Model model) {
		//		//quizを取得（optionalでラップ）問題

		Integer IntegerBeforeId = 0;
		Optional<Quiz> quizOpt = service.selectOneRandomQuiz();

		String rnd_1 = null;
		String rnd_2 = null;
		String rnd_3 = null;
		String rnd_4 = null;

		int counter=0;
		//値が入ってるかの判定
		if (quizOpt.isPresent()) {

			//quizformへの詰めなおし
			Optional<QuizForm> quizFormOpt = quizOpt.map(t -> makeQuizForm(t));
			quizForm = quizFormOpt.get();

			// 前回出題された問題を避ける処理
			if (BeforeId != null) {
				IntegerBeforeId = BeforeId;

				if (IntegerBeforeId == quizForm.getId()) {
						
					while (quizForm.getId().equals(IntegerBeforeId)) {
						quizOpt = service.selectOneRandomQuiz();
						quizFormOpt = quizOpt.map(t -> makeQuizForm(t));
						quizForm = quizFormOpt.get();
						counter++;
						if(counter==100) {
							break;
						}
					}
				} 
			}
			
			if(counter==100) {
				model.addAttribute("msg", "同じ色の問題がこれ以上存在しません");
				return "playRed";
			}
			// クイズがランダムで取得されたレコードの中味をランダムに表示させる-------------------------------
			// 正解の選択肢
			String CorrectAnswer = quizForm.getAnswer();

			// 不正解の選択肢一覧
			String Incorrect_1 = quizForm.getSelect_one();
			String Incorrect_2 = quizForm.getSelect_two();
			String Incorrect_3 = quizForm.getSelect_three();

			List<String> selects = new ArrayList<String>();

			selects.add(CorrectAnswer);
			selects.add(Incorrect_1);
			selects.add(Incorrect_2);
			selects.add(Incorrect_3);

			Collections.shuffle(selects);

			rnd_1 = selects.get(0);
			rnd_2 = selects.get(1);
			rnd_3 = selects.get(2);
			rnd_4 = selects.get(3);

		} else {
			model.addAttribute("msg", "問題が存在しません");
			return "play";
		}

		//表示用modelへの格納
		model.addAttribute("quizForm", quizForm);
		model.addAttribute("rnd_1", rnd_1);
		model.addAttribute("rnd_2", rnd_2);
		model.addAttribute("rnd_3", rnd_3);
		model.addAttribute("rnd_4", rnd_4);
		model.addAttribute("BeforeId", BeforeId);

		return "play";
	}

	// 緑色問題の出題
		/*単語で学習機能*/
		@PostMapping("/playRed")
		public String showQuizRed(@RequestParam(name = "BeforeId", required = false) Integer BeforeId, QuizForm quizForm,
				QuizForm quizFormSelect1, QuizForm quizFormSelect2, Model model) {
			//		//quizを取得（optionalでラップ）問題

			Integer IntegerBeforeId = 0;
			Optional<Quiz> quizOpt = service.selectOneRedRandomQuiz();

			String rnd_1 = null;
			String rnd_2 = null;
			String rnd_3 = null;
			String rnd_4 = null;

			int counter=0;
			//値が入ってるかの判定
			if (quizOpt.isPresent()) {

				//quizformへの詰めなおし
				Optional<QuizForm> quizFormOpt = quizOpt.map(t -> makeQuizForm(t));
				quizForm = quizFormOpt.get();

				// 前回出題された問題を避ける処理
				if (BeforeId != null) {
					IntegerBeforeId = BeforeId;

					if (IntegerBeforeId == quizForm.getId()) {
							
						while (quizForm.getId().equals(IntegerBeforeId)) {
							quizOpt = service.selectOneRedRandomQuiz();
							quizFormOpt = quizOpt.map(t -> makeQuizForm(t));
							quizForm = quizFormOpt.get();
							counter++;
							if(counter==100) {
								break;
							}
						}
					} 
				}
				
				if(counter==100) {
					model.addAttribute("msg", "同じ色の問題がこれ以上存在しません");
					return "playRed";
				}

				// クイズがランダムで取得されたレコードの中味をランダムに表示させる
				// 正解の選択肢
				String CorrectAnswer = quizForm.getAnswer();

				// 不正解の選択肢一覧
				String Incorrect_1 = quizForm.getSelect_one();
				String Incorrect_2 = quizForm.getSelect_two();
				String Incorrect_3 = quizForm.getSelect_three();

				List<String> selects = new ArrayList<String>();

				selects.add(CorrectAnswer);
				selects.add(Incorrect_1);
				selects.add(Incorrect_2);
				selects.add(Incorrect_3);

				Collections.shuffle(selects);

				rnd_1 = selects.get(0);
				rnd_2 = selects.get(1);
				rnd_3 = selects.get(2);
				rnd_4 = selects.get(3);

			} else {
				model.addAttribute("msg", "問題が存在しません");
				return "play";
			}

			//表示用modelへの格納
			model.addAttribute("quizForm", quizForm);
			model.addAttribute("rnd_1", rnd_1);
			model.addAttribute("rnd_2", rnd_2);
			model.addAttribute("rnd_3", rnd_3);
			model.addAttribute("rnd_4", rnd_4);
			model.addAttribute("BeforeId", BeforeId);

			return "playRed";
		}

	// 黄色問題の出題
		/*単語で学習機能*/
		@PostMapping("/playYellow")
		public String showQuizYellow(@RequestParam(name = "BeforeId", required = false) Integer BeforeId, QuizForm quizForm,
				QuizForm quizFormSelect1, QuizForm quizFormSelect2, Model model) {
			//		//quizを取得（optionalでラップ）問題

			Integer IntegerBeforeId = 0;
			Optional<Quiz> quizOpt = service.selectOneYellowRandomQuiz();

			String rnd_1 = null;
			String rnd_2 = null;
			String rnd_3 = null;
			String rnd_4 = null;

			int counter=0;
			//値が入ってるかの判定
			if (quizOpt.isPresent()) {

				//quizformへの詰めなおし
				Optional<QuizForm> quizFormOpt = quizOpt.map(t -> makeQuizForm(t));
				quizForm = quizFormOpt.get();

				// 前回出題された問題を避ける処理
				if (BeforeId != null) {
					IntegerBeforeId = BeforeId;

					if (IntegerBeforeId == quizForm.getId()) {
							
						while (quizForm.getId().equals(IntegerBeforeId)) {
							quizOpt = service.selectOneYellowRandomQuiz();
							quizFormOpt = quizOpt.map(t -> makeQuizForm(t));
							quizForm = quizFormOpt.get();
							counter++;
							if(counter==100) {
								break;
							}
						}
					} 
				}
				
				if(counter==100) {
					model.addAttribute("msg", "同じ色の問題がこれ以上存在しません");
					return "play";
				}

				// クイズがランダムで取得されたレコードの中味をランダムに表示させる
				// 正解の選択肢
				String CorrectAnswer = quizForm.getAnswer();

				// 不正解の選択肢一覧
				String Incorrect_1 = quizForm.getSelect_one();
				String Incorrect_2 = quizForm.getSelect_two();
				String Incorrect_3 = quizForm.getSelect_three();

				List<String> selects = new ArrayList<String>();

				selects.add(CorrectAnswer);
				selects.add(Incorrect_1);
				selects.add(Incorrect_2);
				selects.add(Incorrect_3);

				Collections.shuffle(selects);

				rnd_1 = selects.get(0);
				rnd_2 = selects.get(1);
				rnd_3 = selects.get(2);
				rnd_4 = selects.get(3);

			} else {
				model.addAttribute("msg", "問題が存在しません");
				return "play";
			}

			//表示用modelへの格納
			model.addAttribute("quizForm", quizForm);
			model.addAttribute("rnd_1", rnd_1);
			model.addAttribute("rnd_2", rnd_2);
			model.addAttribute("rnd_3", rnd_3);
			model.addAttribute("rnd_4", rnd_4);
			model.addAttribute("BeforeId", BeforeId);

			return "playYellow";
		}

	// 緑色問題の出題
	/*単語で学習機能*/
	@PostMapping("/playGreen")
	public String showQuizGreen(@RequestParam(name = "BeforeId", required = false) Integer BeforeId, QuizForm quizForm,
			QuizForm quizFormSelect1, QuizForm quizFormSelect2, Model model) {
		//		//quizを取得（optionalでラップ）問題

		Integer IntegerBeforeId = 0;
		Optional<Quiz> quizOpt = service.selectOneGreenRandomQuiz();

		String rnd_1 = null;
		String rnd_2 = null;
		String rnd_3 = null;
		String rnd_4 = null;

		int counter=0;
		//値が入ってるかの判定
		if (quizOpt.isPresent()) {

			//quizformへの詰めなおし
			Optional<QuizForm> quizFormOpt = quizOpt.map(t -> makeQuizForm(t));
			quizForm = quizFormOpt.get();

			// 前回出題された問題を避ける処理
			if (BeforeId != null) {
				IntegerBeforeId = BeforeId;

				if (IntegerBeforeId == quizForm.getId()) {
						
					while (quizForm.getId().equals(IntegerBeforeId)) {
						quizOpt = service.selectOneGreenRandomQuiz();
						quizFormOpt = quizOpt.map(t -> makeQuizForm(t));
						quizForm = quizFormOpt.get();
						counter++;
						if(counter==100) {
							break;
						}
					}
				} 
			}
			
			if(counter==100) {
				model.addAttribute("msg", "同じ色の問題がこれ以上存在しません");
				return "playGreen";
			}

			// クイズがランダムで取得されたレコードの中味をランダムに表示させる
			// 正解の選択肢
			String CorrectAnswer = quizForm.getAnswer();

			// 不正解の選択肢一覧
			String Incorrect_1 = quizForm.getSelect_one();
			String Incorrect_2 = quizForm.getSelect_two();
			String Incorrect_3 = quizForm.getSelect_three();

			List<String> selects = new ArrayList<String>();

			selects.add(CorrectAnswer);
			selects.add(Incorrect_1);
			selects.add(Incorrect_2);
			selects.add(Incorrect_3);

			Collections.shuffle(selects);

			rnd_1 = selects.get(0);
			rnd_2 = selects.get(1);
			rnd_3 = selects.get(2);
			rnd_4 = selects.get(3);

		} else {
			model.addAttribute("msg", "問題が存在しません");
			return "play";
		}

		//表示用modelへの格納
		model.addAttribute("quizForm", quizForm);
		model.addAttribute("rnd_1", rnd_1);
		model.addAttribute("rnd_2", rnd_2);
		model.addAttribute("rnd_3", rnd_3);
		model.addAttribute("rnd_4", rnd_4);
		model.addAttribute("BeforeId", BeforeId);

		return "playGreen";
	}

	/*単語の正解不正解を判定する*/
	@PostMapping("/check")
	public String checkQuiz(QuizForm quizForm, @RequestParam String answer, Model model) {

		if (service.checkQuiz(quizForm.getId(), answer)) {
			model.addAttribute("msg", "正解");
		} else {
			model.addAttribute("msg", "不正解");
		}

		// 出題問題を取り出す(正解のもの)　不正解選択肢はanswerに格納されてる
		Integer id = quizForm.getId();
		Integer BeforeId = id;
		Optional<Quiz> optQuiz = service.selectOneById(id);

		Quiz quiz = optQuiz.get();

		String OutputQuestion = quiz.getQuestion();
		String CorrectAnswer = quiz.getAnswer();
		model.addAttribute("OutputQuestion", OutputQuestion);
		model.addAttribute("CorrectAnswer", CorrectAnswer);
		model.addAttribute("BeforeId", BeforeId);

		return "answer";
	}

	/*赤難易度の単語の正解不正解を判定する*/
	@PostMapping("/checkRed")
	public String checkQuizRed(QuizForm quizForm, @RequestParam String answer, Model model) {

		if (service.checkQuiz(quizForm.getId(), answer)) {
			model.addAttribute("msg", "正解");
		} else {
			model.addAttribute("msg", "不正解");
		}

		// 出題問題を取り出す(正解のもの)　不正解選択肢はanswerに格納されてる
		Integer id = quizForm.getId();
		Integer BeforeId = id;
		Optional<Quiz> optQuiz = service.selectOneById(id);

		Quiz quiz = optQuiz.get();

		String OutputQuestion = quiz.getQuestion();
		String CorrectAnswer = quiz.getAnswer();
		model.addAttribute("OutputQuestion", OutputQuestion);
		model.addAttribute("CorrectAnswer", CorrectAnswer);
		model.addAttribute("BeforeId", BeforeId);

		return "answerRed";
	}

	/*黄単語の正解不正解を判定する*/
	@PostMapping("/checkYellow")
	public String checkQuizYellow(QuizForm quizForm, @RequestParam String answer, Model model) {

		if (service.checkQuiz(quizForm.getId(), answer)) {
			model.addAttribute("msg", "正解");
		} else {
			model.addAttribute("msg", "不正解");
		}

		// 出題問題を取り出す(正解のもの)　不正解選択肢はanswerに格納されてる
		Integer id = quizForm.getId();
		Integer BeforeId = id;
		Optional<Quiz> optQuiz = service.selectOneById(id);

		Quiz quiz = optQuiz.get();

		String OutputQuestion = quiz.getQuestion();
		String CorrectAnswer = quiz.getAnswer();
		model.addAttribute("OutputQuestion", OutputQuestion);
		model.addAttribute("CorrectAnswer", CorrectAnswer);
		model.addAttribute("BeforeId", BeforeId);

		return "answerYellow";
	}

	/*黄単語の正解不正解を判定する*/
	@PostMapping("/checkGreen")
	public String checkQuizGreen(QuizForm quizForm, @RequestParam String answer, Model model) {

		if (service.checkQuiz(quizForm.getId(), answer)) {
			model.addAttribute("msg", "正解");
		} else {
			model.addAttribute("msg", "不正解");
		}

		// 出題問題を取り出す(正解のもの)　不正解選択肢はanswerに格納されてる
		Integer id = quizForm.getId();
		Integer BeforeId = id;
		Optional<Quiz> optQuiz = service.selectOneById(id);

		Quiz quiz = optQuiz.get();

		String OutputQuestion = quiz.getQuestion();
		String CorrectAnswer = quiz.getAnswer();
		model.addAttribute("OutputQuestion", OutputQuestion);
		model.addAttribute("CorrectAnswer", CorrectAnswer);
		model.addAttribute("BeforeId", BeforeId);

		return "answerGreen";
	}
}
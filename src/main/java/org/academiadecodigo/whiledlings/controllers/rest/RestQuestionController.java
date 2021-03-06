package org.academiadecodigo.whiledlings.controllers.rest;

import org.academiadecodigo.whiledlings.commands.OptionDTO;
import org.academiadecodigo.whiledlings.commands.QuestionDTO;
import org.academiadecodigo.whiledlings.converters.OptionToOptionDTO;
import org.academiadecodigo.whiledlings.converters.QuestionTypeOptionsToQuestionTypeOptionsDTO;
import org.academiadecodigo.whiledlings.converters.QuestionTypeTextToQuestionTypeTextDTO;
import org.academiadecodigo.whiledlings.persistence.model.Option;
import org.academiadecodigo.whiledlings.persistence.model.questions.Question;
import org.academiadecodigo.whiledlings.persistence.model.questions.QuestionType;
import org.academiadecodigo.whiledlings.persistence.model.questions.QuestionTypeOptions;
import org.academiadecodigo.whiledlings.persistence.model.questions.QuestionTypeText;
import org.academiadecodigo.whiledlings.services.QuestionService;
import org.graalvm.compiler.nodes.calc.IntegerDivRemNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/question")
public class RestQuestionController {

    private QuestionService questionService;
    private OptionToOptionDTO optionToOptionDTO;
    private QuestionTypeOptionsToQuestionTypeOptionsDTO questTyOptToQuestTyOptDTO;
    private QuestionTypeTextToQuestionTypeTextDTO questTyTextToQuestTyTextDTO;

    @Autowired
    public void setQuestTyTextToQuestTyTextDTO(QuestionTypeTextToQuestionTypeTextDTO questTyTextToQuestTyTextDTO) {
        this.questTyTextToQuestTyTextDTO = questTyTextToQuestTyTextDTO;
    }

    @Autowired
    public void setQuestTyOptToQuestTyOptDTO(QuestionTypeOptionsToQuestionTypeOptionsDTO questTyOptToQuestTyOptDTO) {
        this.questTyOptToQuestTyOptDTO = questTyOptToQuestTyOptDTO;
    }

    @Autowired
    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Autowired
    public void setOptionToOptionDTO(OptionToOptionDTO optionToOptionDTO) {
        this.optionToOptionDTO = optionToOptionDTO;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{qid}/options")
    public ResponseEntity<?> showOptions(@PathVariable Integer qid){

        Question question = questionService.getQuestion(qid);

        if (question.getQuestionType() == QuestionType.TEXT){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        QuestionTypeOptions questionTypeOptions = (QuestionTypeOptions) question;
        List<OptionDTO> optionsList = new ArrayList<>();

        for (Option option : questionTypeOptions.getOptions()){

            optionsList.add(optionToOptionDTO.convert(option));
        }

        return new ResponseEntity<>(optionsList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{qid}")
    public ResponseEntity<QuestionDTO> showQuestion(@PathVariable Integer qid){

        QuestionDTO questionDTO;

        Question question = questionService.getQuestion(qid);

        if (question instanceof QuestionTypeText) {
            questionDTO = questTyTextToQuestTyTextDTO.convert((QuestionTypeText) question);
            return new ResponseEntity<>(questionDTO, HttpStatus.OK);
        }

        questionDTO = questTyOptToQuestTyOptDTO.convert((QuestionTypeOptions) question);
        return new ResponseEntity<>(questionDTO, HttpStatus.OK);
    }
}

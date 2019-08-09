$(document).ready(function () {

    var first = true;
    var idPerson;
    var lastQuestion = false;
    var nextQuestion = 1;
    var opt1next;
    var opt2next;

    $('#submitButtonName').click(function(){

        $.ajax({
            url: 'http://localhost:8080/safeplace/api/',
            type: 'POST',
            data: JSON.stringify({
                name: $('#textBar').val(),
            }),
            async: true,
            contentType: 'application/json',
            success: successCallback,
            error: errorCallback
        });

        hideAll();
        getNextQuestion(nextQuestion);

    });

    $('#submitButton').click(function () {

        hideAll();
        if (lastQuestion === true) {
            return;
        }
        getNextQuestion(nextQuestion);

    });

    $('#multipleChoice1').click(function () {
        nextQuestion = opt1next;
        getNextQuestion(nextQuestion);
    });

    $('#multipleChoice2').click(function () {
        nextQuestion = opt2next;
        getNextQuestion(nextQuestion);
    });

    function getNextQuestion(idNext) {

        if(lastQuestion === true) {
            window.location.href="thank-you";
            return;
        }

        $.ajax({
            url: 'http://localhost:8080/safeplace/api/question/' + idNext,
            type: 'GET',
            async: true,
            contentType: 'application/json',
            success: successCallbackQuestion,
            error: errorCallback
        });
    }

    function successCallback(response) {

        if(first === true) {
            idPerson = response.id;
            first = false;
            return;
        }
       
    }

    function successCallbackQuestion(response) {

        $('#question').text(response.question);

        lastQuestion = response.lastQuestion;

        if (response.idNextQuestion !== undefined) {
            $('#textBar').val('');
            $('#textBar').show();
            $('#submitButton').show();
            nextQuestion = response.idNextQuestion;
            return;
        }

        $('#multipleChoice1').val(response.options[0].option);
        $('#multipleChoice1').show();
        opt1next = response.options[0].idNextQuestion;
        $('#multipleChoice2').val(response.options[1].option);
        $('#multipleChoice2').show();
        opt2next = response.options[1].idNextQuestion;

    }

    function errorCallback() {
        alert(Error);

    }

    function hideAll() {
        $('#textBar').hide();
        $('#submitButtonName').hide();
        $('#submitButton').hide();
        $('#multipleChoise1').hide();
        $('#multipleChoice2').hide();
    }

});








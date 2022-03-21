M.AutoInit();

// card Click

var errorMap = new Map();
errorMap.set(1, "Քննությունը բացակայում է");
errorMap.set(2, "Օգտվողը բացակայում է");
errorMap.set(3, "Տվյալ օգտվողը արդեն կցված է ակտիվ կամ ընթացիկ քննությանը");
errorMap.set(4, "Օգտվողը արդեն ավելացվել է, կամ չունի համապատասխան դեր քննության մասնակից դառնալու համար");

let cardClick = document.querySelectorAll('.card-click');
if (cardClick) {
    for (let i = 0; i < cardClick.length; i++) {
        cardClick[i].addEventListener('click', function (e) {
            let current = document.getElementsByClassName("active");
            current[0].className = current[0].className.replace(" active", "");
            this.className += " active";
        })
    }

}
// answer

let choiceClick = document.querySelectorAll('.choice-item');

if (choiceClick) {
    for (let j = 0; j < choiceClick.length; j++) {
        choiceClick[j].addEventListener('click', function () {
            if (choiceClick[j].classList.contains('result-card')) {
                return;
            }
            for (let i = 0; i < choiceClick.length; i++) {
                choiceClick[i].classList.remove('active');
            }
            choiceClick[j].classList.toggle('active');
        })
    }
}

// call this method from test page in answer click
function answerQuestion(quizQuestionId, answerId, currentQuestionNumber) {
    const Http = new XMLHttpRequest();
    const url = '/member/answerQuestion?questionId=' + quizQuestionId + '&answerId=' + answerId;
    Http.open("GET", url);
    Http.send();
    Http.onreadystatechange = (e) => {
        window.location.href = '/member/quizProcess?questionNumber=' + currentQuestionNumber;
    };
}

// open navbar

function openNav() {
    document.querySelector('#mySidenav').classList.toggle('openNav');
    document.querySelector('#main').classList.toggle('marginMain');
    document.querySelector('.page-header').classList.toggle('opacity');
}

if (document.querySelector('#selectQuizCategory')) {
    let selectQuizCategory = document.querySelector('#selectQuizCategory');
    let selectQuestionCategory = document.querySelector('#selectQuestionCategory');
    selectQuestionCategory.options[selectQuestionCategory.selectedIndex].value = '';
    selectQuizCategory.addEventListener("change", changeQuizCategory, false);
}

if (document.querySelector('#selectQuestionCategory')) {
    let selectQuizCategory = document.querySelector('#selectQuestionCategory');
    selectQuizCategory.addEventListener("change", changeQuizCategory, false);
}

function changeQuizCategory() {
    let selectQuizCategory = document.querySelector('#selectQuizCategory');
    let selectQuestionCategory = document.querySelector('#selectQuestionCategory');
    let selectedOptionQuiz = selectQuizCategory.options[selectQuizCategory.selectedIndex].value;
    let selectedOptionQuestion = selectQuestionCategory.options[selectQuestionCategory.selectedIndex].value;

    if (selectedOptionQuiz == null && selectedOptionQuestion) {
        return
    }
    if (selectedOptionQuiz === '' && selectedOptionQuestion === '') {
        return
    }

    window.location.href = '/commissioner/questions?quizCategoryId=' + selectedOptionQuiz + '&questionCategoryId=' + selectedOptionQuestion;
}


if (document.querySelector('#selectOralQuizCategory')) {
    let selectQuizCategory = document.querySelector('#selectOralQuizCategory');
    let selectQuestionCategory = document.querySelector('#selectOralQuestionCategory');
    selectQuestionCategory.options[selectQuestionCategory.selectedIndex].value = '';
    selectQuizCategory.addEventListener("change", changeQuizCategoryOral, false);
}

if (document.querySelector('#selectPracticQuizCategory')) {
    let selectQuizCategory = document.querySelector('#selectPracticQuizCategory');
    let selectQuestionCategory = document.querySelector('#selectPracticQuestionCategory');
    selectQuestionCategory.options[selectQuestionCategory.selectedIndex].value = '';
    selectQuizCategory.addEventListener("change", changeQuizCategoryPractic, false);
}

if (document.querySelector('#selectOralQuestionCategory')) {
    let selectQuizCategory = document.querySelector('#selectOralQuestionCategory');
    selectQuizCategory.addEventListener("change", changeQuizCategoryOral, false);
}

if (document.querySelector('#selectPracticQuestionCategory')) {
    let selectQuizCategory = document.querySelector('#selectPracticQuestionCategory');
    selectQuizCategory.addEventListener("change", changeQuizCategoryPractic, false);
}

function changeQuizCategoryOral() {
    let selectQuizCategory = document.querySelector('#selectOralQuizCategory');
    let selectQuestionCategory = document.querySelector('#selectOralQuestionCategory');
    let selectedOptionQuiz = selectQuizCategory.options[selectQuizCategory.selectedIndex].value;
    let selectedOptionQuestion = selectQuestionCategory.options[selectQuestionCategory.selectedIndex].value;

    if (selectedOptionQuiz == null && selectedOptionQuestion) {
        return
    }
    if (selectedOptionQuiz === '' && selectedOptionQuestion === '') {
        return
    }

    window.location.href = '/commissioner/oralQuestions?quizCategoryId=' + selectedOptionQuiz + '&oralQuestionCategoryId=' + selectedOptionQuestion;
}

function changeQuizCategoryPractic() {
    let selectQuizCategory = document.querySelector('#selectPracticQuizCategory');
    let selectQuestionCategory = document.querySelector('#selectPracticQuestionCategory');
    let selectedOptionQuiz = selectQuizCategory.options[selectQuizCategory.selectedIndex].value;
    let selectedOptionQuestion = selectQuestionCategory.options[selectQuestionCategory.selectedIndex].value;

    if (selectedOptionQuiz == null && selectedOptionQuestion) {
        return
    }
    if (selectedOptionQuiz === '' && selectedOptionQuestion === '') {
        return
    }

    window.location.href = '/commissioner/practicQuestions?quizCategoryId=' + selectedOptionQuiz + '&practicQuestionCategoryId=' + selectedOptionQuestion;
}


if (document.querySelector('#selectQuizCategoryForQuestionCategory')) {
    let selectQuizCategory = document.querySelector('#selectQuizCategoryForQuestionCategory');
    selectQuizCategory.addEventListener("change", changeQuizCategoryForQuestionCategory, false);
}

function changeQuizCategoryForQuestionCategory() {
    let selectQuizCategory = document.querySelector('#selectQuizCategoryForQuestionCategory');
    let selectedOptionQuiz = selectQuizCategory.options[selectQuizCategory.selectedIndex].value;

    if (selectedOptionQuiz == null || selectedOptionQuiz === "") {
        return
    }

    window.location.href = '/commissioner/questionCategories?quizCategoryId=' + selectedOptionQuiz;
}

if (document.querySelector('#selectQuizCategoryForOralQuestionCategory')) {
    let selectQuizCategory = document.querySelector('#selectQuizCategoryForOralQuestionCategory');
    selectQuizCategory.addEventListener("change", changeQuizCategoryForOralQuestionCategory, false);
}

if (document.querySelector('#selectQuizCategoryForPracticQuestionCategory')) {
    let selectQuizCategory = document.querySelector('#selectQuizCategoryForPracticQuestionCategory');
    selectQuizCategory.addEventListener("change", changeQuizCategoryForPracticQuestionCategory, false);
}

function changeQuizCategoryForOralQuestionCategory() {
    let selectQuizCategory = document.querySelector('#selectQuizCategoryForOralQuestionCategory');
    let selectedOptionQuiz = selectQuizCategory.options[selectQuizCategory.selectedIndex].value;

    if (selectedOptionQuiz == null || selectedOptionQuiz === "") {
        return
    }

    window.location.href = '/commissioner/oralQuestionCategories?quizCategoryId=' + selectedOptionQuiz;
}

function changeQuizCategoryForPracticQuestionCategory() {
    let selectQuizCategory = document.querySelector('#selectQuizCategoryForPracticQuestionCategory');
    let selectedOptionQuiz = selectQuizCategory.options[selectQuizCategory.selectedIndex].value;

    if (selectedOptionQuiz == null || selectedOptionQuiz === "") {
        return
    }

    window.location.href = '/commissioner/practicQuestionCategories?quizCategoryId=' + selectedOptionQuiz;
}

if (document.querySelector('#selectQuizCategoryForSettings')) {
    let selectQuizCategory = document.querySelector('#selectQuizCategoryForSettings');
    selectQuizCategory.addEventListener("change", changeQuizCategoryForSettings, false);
}

function changeQuizCategoryForSettings() {
    let selectQuizCategory = document.querySelector('#selectQuizCategoryForSettings');
    let selectedOptionQuiz = selectQuizCategory.options[selectQuizCategory.selectedIndex].value;

    if (selectedOptionQuiz == null || selectedOptionQuiz === "") {
        return
    }

    window.location.href = '/commissioner/settings?quizCategoryId=' + selectedOptionQuiz;
}

if (document.querySelector('#selectQuizCategoryForReportSettings')) {
    let selectQuizCategory = document.querySelector('#selectQuizCategoryForReportSettings');
    selectQuizCategory.addEventListener("change", changeQuizCategoryForReportSettings, false);
}

function changeQuizCategoryForReportSettings() {
    let selectQuizCategory = document.querySelector('#selectQuizCategoryForReportSettings');
    let selectedOptionQuiz = selectQuizCategory.options[selectQuizCategory.selectedIndex].value;

    if (selectedOptionQuiz == null || selectedOptionQuiz === "") {
        return
    }

    window.location.href = '/commissioner/reportSettings?quizCategoryId=' + selectedOptionQuiz;
}

if (document.querySelector('#selectQuizCategoryAddQuiz')) {
    let selectQuizCategory = document.querySelector('#selectQuizCategoryAddQuiz');
    selectQuizCategory.addEventListener("change", changeQuizCategoryAddQuiz, false);
}

function changeQuizCategoryAddQuiz() {
    let selectQuizCategory = document.querySelector('#selectQuizCategoryAddQuiz');
    let selectedOptionQuiz = selectQuizCategory.options[selectQuizCategory.selectedIndex].value;

    if (selectedOptionQuiz == null || selectedOptionQuiz === "") {
        return
    }

    let id = document.getElementById("quizId").value;
    if (id == null || id === '') {
        window.location.href = '/commissioner/addQuiz?quizCategoryId=' + selectedOptionQuiz;
    } else {
        //window.location.href = '/commissioner/editQuiz?id=' + id + '&quizCategoryId=' + selectedOptionQuiz;
        return;
    }
}

if (document.querySelector('#selectQuizCategoryAddQuestion')) {
    let selectQuizCategory = document.querySelector('#selectQuizCategoryAddQuestion');
    selectQuizCategory.addEventListener("change", changeQuizCategoryAddQuestion, false);
}

function changeQuizCategoryAddQuestion() {
    let selectQuizCategory = document.querySelector('#selectQuizCategoryAddQuestion');
    let selectedOptionQuiz = selectQuizCategory.options[selectQuizCategory.selectedIndex].value;

    if (selectedOptionQuiz == null || selectedOptionQuiz === "") {
        return
    }

    let id = document.getElementById("questionId").value;
    if (id == null || id === '') {
        window.location.href = '/commissioner/addQuestion?quizCategoryId=' + selectedOptionQuiz;
    } else {
        window.location.href = '/commissioner/editQuestion?id=' + id + '&quizCategoryId=' + selectedOptionQuiz;
    }
}


if (document.querySelector('#selectQuizCategoryAddOralQuestion')) {
    let selectQuizCategory = document.querySelector('#selectQuizCategoryAddOralQuestion');
    selectQuizCategory.addEventListener("change", changeQuizCategoryAddOralQuestion, false);
}

if (document.querySelector('#selectQuizCategoryAddPracticQuestion')) {
    let selectQuizCategory = document.querySelector('#selectQuizCategoryAddPracticQuestion');
    selectQuizCategory.addEventListener("change", changeQuizCategoryAddPracticQuestion, false);
}

function changeQuizCategoryAddOralQuestion() {
    let selectQuizCategory = document.querySelector('#selectQuizCategoryAddOralQuestion');
    let selectedOptionQuiz = selectQuizCategory.options[selectQuizCategory.selectedIndex].value;

    if (selectedOptionQuiz == null || selectedOptionQuiz === "") {
        return
    }

    let id = document.getElementById("questionId").value;
    if (id == null || id === '') {
        window.location.href = '/commissioner/addOralQuestion?quizCategoryId=' + selectedOptionQuiz;
    } else {
        window.location.href = '/commissioner/editOralQuestion?id=' + id + '&quizCategoryId=' + selectedOptionQuiz;
    }
}

function changeQuizCategoryAddPracticQuestion() {
    let selectQuizCategory = document.querySelector('#selectQuizCategoryAddPracticQuestion');
    let selectedOptionQuiz = selectQuizCategory.options[selectQuizCategory.selectedIndex].value;

    if (selectedOptionQuiz == null || selectedOptionQuiz === "") {
        return
    }

    let id = document.getElementById("questionId").value;
    if (id == null || id === '') {
        window.location.href = '/commissioner/addPracticQuestion?quizCategoryId=' + selectedOptionQuiz;
    } else {
        window.location.href = '/commissioner/editPracticQuestion?id=' + id + '&quizCategoryId=' + selectedOptionQuiz;
    }
}


if (document.querySelector('#selectQuizCategoryForBooks')) {
    let selectQuizCategory = document.querySelector('#selectQuizCategoryForBooks');
    selectQuizCategory.addEventListener("change", changeQuizCategoryForBooks, false);
}

function changeQuizCategoryForBooks() {
    let selectQuizCategory = document.querySelector('#selectQuizCategoryForBooks');
    let selectedOptionQuiz = selectQuizCategory.options[selectQuizCategory.selectedIndex].value;

    if (selectedOptionQuiz == null || selectedOptionQuiz === "") {
        return
    }

    window.location.href = '/commissioner/books?quizCategoryId=' + selectedOptionQuiz;
}


if (document.querySelector('#selectQuizCategoryForOralBooks')) {
    let selectQuizCategory = document.querySelector('#selectQuizCategoryForOralBooks');
    selectQuizCategory.addEventListener("change", changeQuizCategoryForOralBooks, false);
}

if (document.querySelector('#selectQuizCategoryForPracticBooks')) {
    let selectQuizCategory = document.querySelector('#selectQuizCategoryForPracticBooks');
    selectQuizCategory.addEventListener("change", changeQuizCategoryForPracticBooks, false);
}

function changeQuizCategoryForOralBooks() {
    let selectQuizCategory = document.querySelector('#selectQuizCategoryForOralBooks');
    let selectedOptionQuiz = selectQuizCategory.options[selectQuizCategory.selectedIndex].value;

    if (selectedOptionQuiz == null || selectedOptionQuiz === "") {
        return
    }

    window.location.href = '/commissioner/oralBooks?quizCategoryId=' + selectedOptionQuiz;
}

function changeQuizCategoryForPracticBooks() {
    let selectQuizCategory = document.querySelector('#selectQuizCategoryForPracticBooks');
    let selectedOptionQuiz = selectQuizCategory.options[selectQuizCategory.selectedIndex].value;

    if (selectedOptionQuiz == null || selectedOptionQuiz === "") {
        return
    }

    window.location.href = '/commissioner/practicBooks?quizCategoryId=' + selectedOptionQuiz;
}


// interval timer
if (document.querySelector('.question-time')) {
    let timer = setInterval(customInterval, 1000);

    function customInterval() {
        let ThisSeconds = document.querySelector('#question-time-seconds');
        let timeInSeconds = ThisSeconds.innerText;
        if (timeInSeconds === 1 || timeInSeconds === "1") {
            const Http = new XMLHttpRequest();
            const url = '/member/finishQuiz';
            Http.open("GET", url);
            Http.send();
            Http.onreadystatechange = (e) => {
            };

            return;
        }


        let This = document.querySelector('.question-time');
        timeInSeconds = timeInSeconds - 1;
        let minutes = Math.floor(timeInSeconds / 60);
        let seconds = timeInSeconds % 60;
        let timeFormatted = "";
        if (minutes < 10) {
            timeFormatted += "0"
        }
        timeFormatted += minutes + ":";
        if (seconds < 10) {
            timeFormatted += "0";
        }
        timeFormatted += seconds;

        ThisSeconds.innerText = timeInSeconds;
        This.innerText = timeFormatted;
        if (timeInSeconds <= 5) {
            This.style.color = 'red'
        }
    }

    setInterval(function () {
        let ThisSeconds = document.querySelector('#question-time-seconds');
        let time = ThisSeconds.innerText;
        if (time <= 0) {
            clearInterval(timer);
        }
    }, 1000);

}

$(document).ready(function () {
    setTimeout(function () {
        $('.preloader-custom').fadeOut(100);
    }, 1000)
    scroll();
    interfacePerson();
    scrollOffset();
    $('#search_member').autocomplete({
        serviceUrl: '/commissioner/memberSearch',
        paramName: "tagName",
        transformResult: function (response) {
            return {
                suggestions: $.map($.parseJSON(response), function (item) {
                    return {
                        value: item.firstName + " " + item.lastName + " " + item.patternName,
                        data: item.id, id: item.id
                    };
                })
            }
        },
        onSelect: function (suggestion) {
            let quizId = document.getElementById("quiz_id").value;
            let userId = suggestion.id;
            addQuizMember(quizId, userId);
        }
    });
    $('.datepicker').datepicker({
        format: 'yyyy-m-dd'
    });
    $(document).ready(function () {
        $('.timepicker').timepicker({
            twelveHour: false
        });
    });
    quizProcessScroll();
});

$(window).resize(function () {
    quizProcessScroll();
})


$(document).ready(function () {
    $('#search_commissioner').autocomplete({
        serviceUrl: '/commissioner/commissionerSearch',
        paramName: "tagName",
        transformResult: function (response) {
            return {
                suggestions: $.map($.parseJSON(response), function (item) {
                    return {
                        value: item.firstName + " " + item.lastName + " " + item.patternName,
                        data: item.id, id: item.id
                    };
                })
            }
        },
        onSelect: function (suggestion) {
            let quizId = document.getElementById("quiz_id").value;
            let userId = suggestion.id;
            addQuizCommissioner(quizId, userId);
        }
    });
});

$(document).ready(function () {
    $('#search_test_question').autocomplete({
        serviceUrl: '/commissioner/testQuestionSearch',
        paramName: "tagName",
        transformResult: function (response) {
            return {
                suggestions: $.map($.parseJSON(response), function (item) {
                    return {
                        value: item.questionText,
                        data: item.id, id: item.id
                    };
                })
            }
        },
        onSelect: function (suggestion) {
            let questionId = suggestion.id;
            goToTestQuestion(questionId);
        }
    });
});

$(document).ready(function () {
    $('#search_oral_question').autocomplete({
        serviceUrl: '/commissioner/oralQuestionSearch',
        paramName: "tagName",
        transformResult: function (response) {
            return {
                suggestions: $.map($.parseJSON(response), function (item) {
                    return {
                        value: item.question,
                        data: item.id, id: item.id
                    };
                })
            }
        },
        onSelect: function (suggestion) {
            let questionId = suggestion.id;
            goToOralQuestion(questionId);
        }
    });
});

$(document).ready(function () {
    $('#search_practic_question').autocomplete({
        serviceUrl: '/commissioner/practicQuestionSearch',
        paramName: "tagName",
        transformResult: function (response) {
            return {
                suggestions: $.map($.parseJSON(response), function (item) {
                    return {
                        value: item.question,
                        data: item.id, id: item.id
                    };
                })
            }
        },
        onSelect: function (suggestion) {
            let questionId = suggestion.id;
            goToPracticQuestion(questionId);
        }
    });
});

$(document).ready(function () {
    $('.autocomplete-suggestions').css('width','960px');
    $('#search_finished_quiz').autocomplete({
        serviceUrl: '/commissioner/quizSearch',
        paramName: "tagName",
        transformResult: function (response) {
            return {
                suggestions: $.map($.parseJSON(response), function (item) {
                    $('.autocomplete-suggestions').css('width','960px');
                    return {
                        value: item.name + " | " + item.selectedStartDate,
                        data: item.id, id: item.id
                    };
                })
            }
        },
        onSelect: function (suggestion) {
            let quizId = suggestion.id;
            goToQuiz(quizId);
        }
    });
});

$(document).ready(function () {
    $('.autocomplete-suggestions').css('width','960px');
    $('#search_past_member').autocomplete({
        serviceUrl: '/commissioner/pastMemberSearch',
        paramName: "tagName",
        transformResult: function (response) {
            return {
                suggestions: $.map($.parseJSON(response), function (item) {
                    $('.autocomplete-suggestions').css('width','960px');
                    return {
                        value: item.firstName + " " + item.lastName + " " + item.patternName,
                        data: item.id, id: item.id
                    };
                })
            }
        },
        onSelect: function (suggestion) {
            let userId = suggestion.id;
            goToPastUser(userId);
        }
    });
});

function quizProcessScroll() {
    if (document.querySelector('.question-answer') != null) {
        let height = document.querySelector('.question-answer').offsetHeight;
        document.querySelector('.scroll-bar').style.height = height
    }
}

function goToTestQuestion(questionId) {
    const Http = new XMLHttpRequest();
    const url = '/commissioner/editQuestion?id=' + questionId;
    Http.open("GET", url);
    Http.send();
    Http.onreadystatechange = (e) => {
        window.location.href = '/commissioner/editQuestion?id=' + questionId;
    };
}

function goToOralQuestion(questionId) {
    const Http = new XMLHttpRequest();
    const url = '/commissioner/editOralQuestion?id=' + questionId;
    Http.open("GET", url);
    Http.send();
    Http.onreadystatechange = (e) => {
        window.location.href = '/commissioner/editOralQuestion?id=' + questionId;
    };
}

function goToPracticQuestion(questionId) {
    const Http = new XMLHttpRequest();
    const url = '/commissioner/editPracticQuestion?id=' + questionId;
    Http.open("GET", url);
    Http.send();
    Http.onreadystatechange = (e) => {
        window.location.href = '/commissioner/editPracticQuestion?id=' + questionId;
    };
}

function goToQuiz(quizId) {
    const Http = new XMLHttpRequest();
    const url = '/commissioner/viewQuiz?id=' + quizId;
    Http.open("GET", url);
    Http.send();
    Http.onreadystatechange = (e) => {
        window.location.href = '/commissioner/viewQuiz?id=' + quizId;
    };
}

function goToPastUser(userId) {
    const Http = new XMLHttpRequest();
    const url = '/commissioner/viewUser?userId=' + userId;
    Http.open("GET", url);
    Http.send();
    Http.onreadystatechange = (e) => {
        window.location.href = '/commissioner/viewUser?userId=' + userId;
    };
}

function addQuizMember(quizId, userId) {
    const Http = new XMLHttpRequest();
    const url = '/commissioner/addQuizMember?quizId=' + quizId + '&userId=' + userId;
    Http.open("GET", url);
    Http.send();
    Http.onreadystatechange = (e) => {
        window.location.href = '/commissioner/quizMembersAdd?id=' + quizId;
    };
}

function addQuizCommissioner(quizId, userId) {
    const Http = new XMLHttpRequest();
    const url = '/commissioner/addQuizCommissioner?quizId=' + quizId + '&userId=' + userId;
    Http.open("GET", url);
    Http.send();
    Http.onreadystatechange = (e) => {
        window.location.href = '/commissioner/quizCommissionersAdd?id=' + quizId;
    };
}

function scroll() {
    let windHeight = window.innerHeight;
    let bodyHeight = document.body.offsetHeight;

    if (windHeight < bodyHeight) {
        if (document.querySelector('.form-button')) {
            document.querySelector('.form-button').classList.add('form-button-fixed');
            document.querySelector('form').style.paddingBottom = ''
        }

    }
}

function interfacePerson() {
    let sidenav = document.querySelector('.sidenav1');
    if (!sidenav) {
        document.querySelector('#main').classList.remove('marginMain');
        document.querySelector('#main').style.marginLeft = '0';
    }
}

function scrollOffset() {
    let activeDiv = document.querySelector('.card-click.active');
    if (activeDiv) {
        let offsetDiv = activeDiv.offsetTop;
        if (offsetDiv >= 300) {
            console.log(offsetDiv)
            console.log(Number(offsetDiv - 260))
            document.querySelector('.scroll-bar').scroll(0, offsetDiv - 260);
        }
    }
}

let btnClick = document.querySelector('.end-btn');

if (btnClick) {
    let noBtn = document.querySelector('.btn-no');
    btnClick.addEventListener('click', function () {
        document.querySelector('.dialog-process-body').style.display = 'block';
    });
    noBtn.addEventListener('click', function () {
        document.querySelector('.dialog-process-body').style.display = 'none';
    })
}

function clickTab(tab) {
    window.location.href = tab;
}

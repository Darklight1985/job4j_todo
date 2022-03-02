
    function check() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/todo/tasks",
        data: {'finalized'  : $('#checkTask').prop('checked')
        },
        dataType: "json",
        success: function (data) {
            $('#table tbody tr').remove()
            checkTask(data);
        }
    })
}

    function pass(elem) {
    var status = elem.target.value
    $.ajax({
    type: 'POST',
    url: 'http://localhost:8080/todo/putitem',
    data: {
    'descrip': status
},
    success: function () {
    check()
}
})
}

    function addRow() {
    $.ajax({
        type: 'POST',
        url: 'http://localhost:8080/todo/tasks',
        data: {
            'descrip': $('#descr').val()
        },
        dataType: 'json',
        success: function (data) {
            $('#table tbody tr').remove()
            checkTask(data);
        }
    })
}

    function checkTask(data) {
    for (let i = 0; i < data.length; i++) {
    $tr = document.createElement('tr')

    $td1 = document.createElement('td')
    $td1.innerHTML = parseInt(i+1)

    $td2 = document.createElement('td')
    $td2.innerHTML = data[i]['description']

    $td3 = document.createElement('td')
    $td3.innerHTML = data[i]['created']

    $tr.appendChild($td1)
    $tr.appendChild($td2)
    $tr.appendChild($td3)

    $td4 = document.createElement('td')

    $newDiv = document.createElement('div')
    $newDiv.className = "form-check"

    $newlabel = document.createElement('label')
    $newlabel.className = "form-check-label"
    $newlabel.htmlFor = "defaultCheck1"

        $newCheckBox = document.createElement('input')
    $newCheckBox.id = "checkBox";
    $newCheckBox.type = "checkBox";
    $newCheckBox.value = data[i]['description']
    $newCheckBox.className = "form-check-input";
    if (data[i]['done'] === true) {
    $newCheckBox.checked = true;
    $newlabel.innerText = "Выполнено"
} else {
    $newCheckBox.checked = false;
    $newlabel.innerText = "Не выполнено"
}
    $newCheckBox.addEventListener("change", pass, true)

    $newDiv.appendChild($newCheckBox)
    $newDiv.appendChild($newlabel)

    $td4.appendChild($newDiv)

    $tr.appendChild($td4)

    $('#table tbody').append($tr);
}
}


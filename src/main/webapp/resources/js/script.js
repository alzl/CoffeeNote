// JavaScript 함수: 버튼 클릭 시 호출되어 버튼의 텍스트를 알림으로 표시
function showAlert(button) {
	var buttonText = button.innerText; // 클릭된 버튼의 텍스트를 가져옴
	alert("You clicked: " + buttonText); // 텍스트를 알림으로 표시
}
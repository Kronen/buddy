$(document).ready(main);

function main() {
	registerSideNav();
	registerCloseAlert();
}

function registerSideNav() {
	$(".sidenav").sidenav();
}

function registerCloseAlert() {
	$('#alert-error-close').click(function() {
		$("#alert-error").fadeOut("slow", function() {});
	});
	$('#alert-logout-close').click(function() {
		$("#alert-logout").fadeOut("slow", function() {});
	});
}
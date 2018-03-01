$(document).ready(main);

function main() {
	registerSideNav();
	registerCloseAlert();

	validateForms();
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

function validateForms() {
    $.validator.setDefaults({
        errorClass: 'invalid',
        validClass: "valid",
        errorElement: 'span',
        errorPlacement: function(error, element) {
            var $label = $(element)
                .closest("form")
                .find("label[for='" + element.attr("id") + "']");
            if($label.next().hasClass('helper-text')) {
                $label.next().attr('data-error', error.text())
            } else {
                var $errorSpan = $('<span />').attr({
                    'class': 'helper-text',
                    'data-error': error.text()});
                $label.after($errorSpan)
            }
        },
        messages: {
            email: {
                required: "We need your email address to contact you",
                email: "The input is not a valid email address"
            },
            firstName: {
                required: "We need your first name"
            },
            lastName: {
                required: "We need your last name"
            },
            feedback: {
                required: "Your feedback is valued and required"
            },
            confirmPassword: {
                equalTo: "The password and its confirmation are not the same"
            }
        }
    });
    $("#contactForm").validate({
        rules: {
            email: {
                required: true,
                email: true
            },
            firstName: {
                required: true
            },
            lastName: {
                required: true
            },
            feedback: {
                required: true
            }
        },

    });
    $("#forgotPasswordForm").validate({
        rules: {
            email: {
                required: true,
                email: true
            }
        }
    });
    $("#savePasswordForm").validate({
        rules: {
            password: "required",
            confirmPassword: {
                required: true,
                equalTo: "#password"
            }
        }
    });


}
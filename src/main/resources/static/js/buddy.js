$(document).ready(main);

function main() {
	initSideNav();
	initCloseAlert();
    initCountrySelect();

	validateForms();
}

function initSideNav() {
	$(".sidenav").sidenav();
}

function initCountrySelect() {
    // Initialize country select in sign up form
    $('#country').formSelect();
}

function initCloseAlert() {
	$('#alert-error-close').click(function() {
		$("#alert-error").fadeOut("slow", function() {});
	});
	$('#alert-logout-close').click(function() {
		$("#alert-logout").fadeOut("slow", function() {});
	});
}

function validateForms() {
    $.validator.addMethod('phone', function (value, element) {
        return this.optional(element) || /^\+?[0-9\-\(\)\s]{7,16}$/.test(value);
    }, "Please enter a valid phone number");

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
            },
            description: {
                maxlength: $.validator.format("Please enter no more than {0} characters")
            }        }
    });
    $("#signUpForm").validate({
        rules: {
            username: {
                required: true
            },
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
            password: "required",
            confirmPassword: {
                required: true,
                equalTo: "#password"
            },
            description: {
                maxlength: 300
            },
            phoneNumber: 'phone'
        },
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
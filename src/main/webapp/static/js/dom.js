import {dataHandler} from "./data_handler.js";

export let dom = {
    init: function () {
        dom.addEventToCartButtons();
        dom.addEventToQuantityInput();
        dom.addEventToItems();
        dom.addEventToCheckoutCheckbox();
        dom.changeVisibility();
        dom.addEventToUserLoginLogout();
        dom.showLoggedIn();
        dom.addModalValidation();
        document.querySelector("#password").addEventListener("keyup", dom.checkStrength);


    },
    addEventToUserLoginLogout: function () {
        document.querySelector('#logout').addEventListener('click', dom.logout);
        document.querySelector('#login').addEventListener('click', dom.login);
        document.querySelector('#register').addEventListener('click', dom.register);
    },
    addEventToCheckoutCheckbox: function () {
        let a = document.querySelector('.checkout-checkbox');
        if (a != null) {
            document.querySelector('.checkout-checkbox').addEventListener('change', dom.changeVisibility);

        }
    },
    addEventToCartButtons: function () {
        let add_cart_btn = document.querySelectorAll(".add-to-cart");
        for (let btn of add_cart_btn) {
            btn.addEventListener('click', dom.addToCart);
        }
    },
    addEventToQuantityInput: function () {
        let inputs = document.querySelectorAll('.quantity-num');
        for (let input of inputs) {
            input.addEventListener('click', dom.handleQuantityValue);
            input.addEventListener('keyup', dom.handleQuantityValue);
        }
    },
    addEventToItems: function () {
        let items = document.querySelectorAll('.item');
        for (let item of items) {
            item.addEventListener('click', dom.editCart);
        }
    },
    addModalValidation: function () {
        $(document).ready(function () {

            $("#inputForm").validate({
                rules: {
                    username: {
                        required: true,
                        minlength: 6
                    },
                    password: "required",
                    email: "required"
                },
                messages: {
                    username: {
                        required : '<span class="hide block-help text-danger"><i class="fa fa-info-circle text-danger" aria-hidden="true"></i> Please enter your user name!</span>',
                        //required: '<i class="fa fa-info-circle text-danger prefix" aria-hidden="true"></i><p class="text-danger">Please provide your user name!"</p>',
                      //  required: '<p class="text-danger"><b>Please provide your user name!"</b></p>',
                        minlength: '<span class="hide block-help text-danger"><i class="fa fa-info-circle text-danger" aria-hidden="true"></i> Your user name must be at least 6 characters!</span>'
                    },
                    password: '<span class="hide block-help text-danger"><i class="fa fa-info-circle text-danger" aria-hidden="true"></i> Please provide your password!</span>',
                    email: '<span class="hide block-help text-danger"><i class="fa fa-info-circle text-danger" aria-hidden="true"></i> Please enter a valid email address!</span>'
                }

            });

        });


    },
    addToCart: function (event) {
        let prodId = event.target.dataset.itemId;
        dataHandler.addToCart(prodId, function (response) {
            document.querySelector(".shopping_cart_icon span").dataset.count = response["cartSize"];
        })
    },
    changeVisibility: function () {

        let shippingDiv = document.querySelector('.checkout-shipping-address');
        let cb = document.querySelector('.checkout-checkbox');

        if (cb != null) {
            if (cb.checked === true) {
                dom.changeInputReguired(false);
                shippingDiv.style.display = 'none';
            } else {
                shippingDiv.style.display = 'block';
                dom.changeInputReguired(true);
            }
        }
    },
    changeInputReguired: function (mode) {
        let shipping_inputs = document.querySelectorAll(".shipping");
        for (let shipping_input of shipping_inputs)
            shipping_input.required = mode;
    },
    editCart: function (e) {
        let item = e.currentTarget;
        let classList = e.target.classList;
        if (classList.contains("refresh") || classList.contains("remove")) {
            let input = e.currentTarget.querySelector(".quantity-num");
            let prodId = e.currentTarget.dataset.prodId;
            let newProdsNumber = classList.contains("remove") ? 0 : input.value;

            dataHandler.editCart(prodId, newProdsNumber.toString(), function (response) {
                let newProdPrice = response["prodTotalPrice"];
                let totalPrice = response["totalPrice"];
                if (classList.contains("remove")) {
                    if (item.parentNode.children.length === 1) window.location.reload();
                    item.remove();
                }
                item.querySelector(".subtotal-num").textContent = `$${newProdPrice.toFixed(1)}`;
                document.querySelector(".total").textContent = `Total $${totalPrice.toFixed(1)}`;
                input.dataset.currentNum = input.value;
            })
        }
    },
    handleQuantityValue: function (e) {
        let value = e.target.value;
        if (parseInt(value) < 1 || e.key === "e") {
            e.target.value = 1;
        }
    },
    openModal: function (title, button_text, callback) {

        let form_values = {};

        $('#inputLabel').text(title);
        $('#modalErrorMessage').text("");
        if (button_text === "Login") {
            dom.setModalForLogin('none')
        } else {
            dom.setModalForLogin('block');
        }

        let $inputModal = $('#inputModal');

        $inputModal.validate().resetForm();
        $inputModal.modal({show: true});
        let $submitButton = $('#submit-button');
        $submitButton.off();
        $submitButton.text(button_text);
        $submitButton.click(function () {
            let $inputs = $('#inputForm :input');
            $inputs.each(function () {
                form_values[this.name] = $(this).val();
            });
            callback(form_values)
        })

    },
    setModalForLogin: function(displayType){
        $('#user-email').css('display', displayType);
        $('#user-password_confirm').css('display', displayType);
        $('#popover-password').css('display', displayType);
    },
    setLoginData: function (results) {

        if (results["success"] === "true") {
            sessionStorage.setItem('username', results["username"]);
            sessionStorage.setItem('userid', results["userid"]);
            dom.showLoggedIn();
            location.reload();
        } else {
            document.querySelector("#modalErrorMessage").innerHTML = `${results["type"]} failed. ${results["message"]}`;
        }
    },
    login: function () {
        dom.openModal('Login', 'Login', function (form_values) {
            if ($("#inputForm").valid()) {
                dataHandler.handleUserAuthentication('/login', form_values, function (results) {
                    dom.setLoginData(results);
                });
            }
        })
    },
    register: function () {

        dom.openModal('Register', 'Register', function (form_values) {
            if (($("#inputForm").valid()) && (dom.checkPasswords()) && dom.checkStrength()) {
                dataHandler.handleUserAuthentication('/register', form_values, function (results) {
                    dom.setLoginData(results);
                });
            }
        });
    },
    showLoggedIn: function () {
        let username = sessionStorage.getItem("username");
        let register = document.querySelector("#register");
        let login = document.querySelector("#login");
        let logout = document.querySelector("#logout");
        let navbar = document.querySelector("#navbar-text");
        if (username) {
            navbar.style.display = 'inline';
            navbar.innerHTML = 'Welcome <b>' + username + '</b>!';
            register.style.display = 'none';
            login.style.display = 'none';
            logout.style.display = 'block';
        } else {
            navbar.style.display = 'none';
            register.style.display = 'block';
            login.style.display = 'block';
            logout.style.display = 'none';
        }
    },
    logout: function () {
        if (sessionStorage.getItem("username"))
            sessionStorage.removeItem("username");
        sessionStorage.removeItem("userid");
        dataHandler.handleUserAuthentication('/logout', null, function (results) {
        });
        location.reload();
    },

    checkPasswords: function () {
        let password = document.querySelector("#password").value;
        let password_check = document.querySelector("#password_confirm").value;
        if (password !== password_check) {
            document.querySelector("#modalErrorMessage").innerHTML = "Passwords not same";
            return false;
        }

        document.querySelector("#modalErrorMessage").innerHTML = "";
        return true;


    },
    checkStrength: function () {
        let strength = 0;
        let password = document.querySelector("#password").value;

        //If password contains both lower and uppercase characters, increase strength value.
        if (password.match(/([a-z].*[A-Z])|([A-Z].*[a-z])/)) {
            strength += 1;
            $('.low-upper-case').addClass('text-success');
            $('.low-upper-case i').removeClass('fa-times').removeClass('text-danger').addClass('fa-check');
            $('#popover-password-top').addClass('hide');


        } else {
            $('.low-upper-case').removeClass('text-success');
            $('.low-upper-case i').addClass('fa-times').addClass('text-danger').removeClass('fa-check');
            $('#popover-password-top').removeClass('hide');
        }

        //If it has numbers and characters, increase strength value.
        if (password.match(/([a-zA-Z])/) && password.match(/([0-9])/)) {
            strength += 1;
            $('.one-number').addClass('text-success');
            $('.one-number i').removeClass('fa-times').removeClass('text-danger').addClass('fa-check');
            $('#popover-password-top').addClass('hide');

        } else {
            $('.one-number').removeClass('text-success');
            $('.one-number i').addClass('fa-times').addClass('text-danger').removeClass('fa-check');
            $('#popover-password-top').removeClass('hide');
        }

        //If it has one special character, increase strength value.
        if (password.match(/([!,%,&,@,#,$,^,*,?,_,~])/)) {
            strength += 1;
            $('.one-special-char').addClass('text-success');
            $('.one-special-char i').removeClass('fa-times').removeClass('text-danger').addClass('fa-check');
            $('#popover-password-top').addClass('hide');

        } else {
            $('.one-special-char').removeClass('text-success');
            $('.one-special-char i').addClass('fa-times').addClass('text-danger').removeClass('fa-check');
            $('#popover-password-top').removeClass('hide');
        }

        if (password.length > 7) {
            strength += 1;
            $('.eight-character').addClass('text-success');
            $('.eight-character i').removeClass('fa-times').removeClass('text-danger').addClass('fa-check');
            $('#popover-password-top').addClass('hide');

        } else {
            $('.eight-character').removeClass('text-success');
            $('.eight-character i').addClass('fa-times').addClass('text-danger').removeClass('fa-check');
            $('#popover-password-top').removeClass('hide');
        }
        // If value is less than 2
        let $result = $('#result');
        let $passwordStrength = $('#password-strength');
        let passwordOk = true;
        if (strength===0){
            $passwordStrength.css('width', '0%');
            return false;
        }
        else if (strength < 2) {
            $result.removeClass()
            $passwordStrength.addClass('progress-bar-danger');
            $result.addClass('text-danger').text('Very Weak');
            $passwordStrength.css('width', '10%');
            passwordOk = false;
        } else if (strength === 2) {
            $result.addClass('good');
            $passwordStrength.removeClass('progress-bar-danger');
            $passwordStrength.addClass('progress-bar bg-warning');
            $result.addClass('text-warning').text('Weak')
            $passwordStrength.css('width', '60%');
            passwordOk = false;
        } else if (strength === 4) {
            $result.removeClass()
            $result.addClass('strong');
            $passwordStrength.removeClass('progress-bar bg-warning');
            $passwordStrength.addClass('progress-bar bg-success');
            $result.addClass('text-success').text('Strength');
            $passwordStrength.css('width', '100%');
        }
        return passwordOk;
    }

}
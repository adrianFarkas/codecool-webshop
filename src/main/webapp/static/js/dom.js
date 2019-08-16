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


    },
    addEventToUserLoginLogout: function(){
        document.querySelector('#logout').addEventListener('click', dom.logout);
        document.querySelector('#login').addEventListener('click', dom.login);
        document.querySelector('#register').addEventListener('click', dom.register);
    },
    addEventToCheckoutCheckbox: function(){
    let a = document.querySelector('.checkout-checkbox');
    if (a!=null) {
        document.querySelector('.checkout-checkbox').addEventListener('change', dom.changeVisibility);

    }
    },
    addEventToCartButtons: function () {
        let add_cart_btn = document.querySelectorAll(".add-to-cart");
        for(let btn of add_cart_btn) {
            btn.addEventListener('click', dom.addToCart);
        }
    },
    addEventToQuantityInput: function() {
        let inputs = document.querySelectorAll('.quantity-num');
        for(let input of inputs) {
            input.addEventListener('click', dom.handleQuantityValue);
            input.addEventListener('keyup', dom.handleQuantityValue);
        }
    },
    addEventToItems: function() {
        let items = document.querySelectorAll('.item');
        for(let item of items) {
            item.addEventListener('click', dom.editCart);
        }
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

        if (cb!=null) {
            if (cb.checked === true) {
                dom.changeInputReguired(false);
                shippingDiv.style.display = 'none';
            } else {
                shippingDiv.style.display = 'block';
                dom.changeInputReguired(true);
            }
        }
    },
    changeInputReguired: function(mode){
        let shipping_inputs = document.querySelectorAll(".shipping");
        for (let shipping_input of shipping_inputs)
            shipping_input.required = mode;
    },
    editCart: function (e) {
        let item = e.currentTarget;
        let classList = e.target.classList;
        if(classList.contains("refresh") || classList.contains("remove")) {
            let input = e.currentTarget.querySelector(".quantity-num");
            let prodId = e.currentTarget.dataset.prodId;
            let newProdsNumber = classList.contains("remove") ? 0 : input.value;

            dataHandler.editCart(prodId, newProdsNumber.toString(), function (response) {
                let newProdPrice = response["prodTotalPrice"];
                let totalPrice = response["totalPrice"];
                if(classList.contains("remove")) {
                    if(item.parentNode.children.length === 1) window.location.reload();
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
        if(parseInt(value) < 1 || e.key === "e") {
            e.target.value = 1;
        }
    },
    openModal: function (title, button_text, callback) {
        let form_values = {};

        $('#inputLabel').text(title);
        $('#inputModal').modal({show: true});
        $('#submit-button').off();
        $('#submit-button').text(button_text);
        $('#submit-button').click(function () {
            let $inputs = $('#inputForm :input');
            $inputs.each(function () {
                form_values[this.name] = $(this).val();
            });
            callback(form_values)
        })
    },
    setLoginData: function (results) {

        if (results["success"] === "true") {
            sessionStorage.setItem('username', results["username"]);
            sessionStorage.setItem('userid', results["userid"]);
            dom.showLoggedIn();
            location.reload();
        } else {
            alert(`${results["type"]} failed`);

        }
    },
    login: function () {
        dom.openModal('Login', 'Login', function (form_values) {
            dataHandler.handleUserAuthentication('/login', form_values, function (results) {
                dom.setLoginData(results);
            });
        });
    },
    register: function () {

        dom.openModal('Register', 'Register', function (form_values) {
            dataHandler.handleUserAuthentication('/register', form_values, function (results) {
                dom.setLoginData(results);
            });
        });
    },
    showLoggedIn: function () {
        let username = sessionStorage.getItem("username");
        let register = document.querySelector("#register");
        let login = document.querySelector("#login");
        let logout = document.querySelector("#logout");
        let navbar = document.querySelector("#navbar-text");
        if (username) {
            navbar.style.display = 'block';
            navbar.innerText = `Signed in as ${username}`;
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
        location.reload();
    }
};
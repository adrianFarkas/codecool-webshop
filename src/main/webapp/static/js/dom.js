import {dataHandler} from "./data_handler.js";

export let dom = {
    init: function () {
        dom.addEventToCartButtons();
        let a = document.querySelector('.checkout-checkbox');
        if (a!=null) {
            document.querySelector('.checkout-checkbox').addEventListener('change', dom.changeVisibility);
            dom.changeVisibility();

        }

    },
    addEventToCartButtons: function () {
        let add_cart_btn = document.querySelectorAll(".add-to-cart");
        for(let btn of add_cart_btn) {
            btn.addEventListener('click', dom.addToCart);
        }
    },
    addToCart: function (event) {
        let prodId = event.target.dataset.itemId;
        dataHandler.addToCart(prodId, function (response) {
            document.querySelector(".shopping_cart span").dataset.count = response["cartSize"];
        })
    },

    changeVisibility: function () {

        let shippingDiv = document.querySelector('.checkout-shipping-address');

    if ( shippingDiv.style.display ==='none') {
        dom.changeInputReguired(true);
        shippingDiv.style.display = 'block';
    }
    else {
        shippingDiv.style.display = 'none';
        dom.changeInputReguired(false);
    }


    },

    changeInputReguired: function(mode){
        let shipping_inputs = document.querySelectorAll(".shipping");
        for (let shipping_input of shipping_inputs)
            shipping_input.required = mode;
    }
};
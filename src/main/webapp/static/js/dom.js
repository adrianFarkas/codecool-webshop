import {dataHandler} from "./data_handler.js";

export let dom = {
    init: function () {
        dom.addEventToCartButtons();
        let a = document.querySelector('.teszt2');
        if (a!=null)
        document.querySelector('.teszt2').addEventListener('change', dom.changeVisibility);

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

        let g = document.querySelector('.checkout-shipping-address');
        let x = g.style.display ;

    if ( g.style.display ==='none')
        g.style.display ='block';
    else
        g.style.display ='none';

    }
};
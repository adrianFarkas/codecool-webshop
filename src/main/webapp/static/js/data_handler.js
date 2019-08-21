export let dataHandler = {
    _api_get: function (url, callback) {
        $.ajax({url,
            type: 'GET',
        }).then(response => callback(response));
    },
    _api_post: function (url, data, callback) {
        $.ajax({url,
            dataType: 'json',
            type: 'POST',
            data: JSON.stringify(data),
        })
            .then(response => callback(response));
    },
    addToCart: function(productId, callback) {
        let data = {
            'productId': productId,
        };
        this._api_post('/add-to-cart', data, (response) => {
            callback(response);
        })
    },
    editCart: function (prodId, newProdsNumber, actProdNum, callback) {
        let data = {
            'productId': prodId,
            'newQuantity': newProdsNumber.toString(),
            'actQuantity' : actProdNum.toString()
        };
        this._api_post('/edit-cart', data, (response) => {
            callback(response);
        })
    }
};

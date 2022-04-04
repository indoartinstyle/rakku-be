function updateOrder(orderToBeUpdated, productsToBeUpdated){
    var listBool = true;
    var context = getContext();
    var collection = getContext().getCollection();
    var order = orderToBeUpdated;
    var products = productsToBeUpdated;

    var isAccepted1 = collection.replaceDocument(order._self, order, {etag: order._etag},
                 function (err, documentCreated) {
                  if (err) throw new Error('Error while updating Order document' + err.message);
                    context.getResponse().setBody(documentCreated)
            });

    products.forEach(product => {
        var isAccepted2 = collection.replaceDocument(product._self, product, {etag: product._etag},
                     function (err, documentCreated) {
                      if (err) throw new Error('Error while updating product document' + err.message);
                        context.getResponse().setBody(documentCreated)
                     });
        lsitBool = listBool && isAccepted2;

    });
    if(!isAccepted1 || !lsitBool) throw new Error('Error while creating document [iais change create - ' + isAccepted + ', Core iais updated - ' + isAccepted1 + ', productToBeUpdated created - ' + isAccepted2 +']');

}
const API_URL = "http://localhost:8080/api/products";

function loadProducts() {

    fetch(API_URL)
        .then(response => response.json())
        .then(products => displayProducts(products))
        .catch(error => console.error(error));
}

function displayProducts(products) {

    const table = document.getElementById("productTable");

    table.innerHTML = "";

    products.forEach(product => {

        const row = `
            <tr>
                <td>${product.id}</td>
                <td>${product.name}</td>
                <td>${product.price}</td>
                <td>${product.stockQuantity}</td>
                <td>${product.reorderThreshold}</td>
<td>
    <button onclick="editProduct(${product.id})">
        Edit
    </button>

    <button onclick="deleteProduct(${product.id})">
        Delete
    </button>
</td>
            </tr>
        `;

        table.innerHTML += row;
    });
}

function searchProducts() {

    const name = document.getElementById("searchInput").value;

    fetch(`${API_URL}/search?name=${name}`)
        .then(response => response.json())
        .then(products => displayProducts(products))
        .catch(error => console.error(error));
}

function deleteProduct(id) {

    fetch(`${API_URL}/${id}`, {
        method: "DELETE"
    })
        .then(response => response.text())
        .then(() => loadProducts());
}
function editProduct(id) {

    fetch(`${API_URL}/${id}`)
        .then(response => response.json())
        .then(product => {

            document.getElementById("productName").value = product.name;
            document.getElementById("productPrice").value = product.price;
            document.getElementById("productStock").value = product.stockQuantity;
            document.getElementById("reorderThreshold").value = product.reorderThreshold;

            document.getElementById("productForm").dataset.editId = id;

            document.querySelector("#productForm button").textContent =
                "Update Product";
        });
}

document.getElementById("productForm").addEventListener("submit", function(event) {

    event.preventDefault();

    const form = document.getElementById("productForm");
    const editId = form.dataset.editId;

    const product = {
        name: document.getElementById("productName").value,
        price: Number(document.getElementById("productPrice").value),
        stockQuantity: Number(document.getElementById("productStock").value),
        reorderThreshold: Number(document.getElementById("reorderThreshold").value)
    };

    const url = editId
        ? `${API_URL}/${editId}`
        : API_URL;

    const method = editId ? "PUT" : "POST";

    fetch(url, {
        method: method,
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(product)
    })
        .then(response => response.json())
        .then(() => {

            form.reset();

            delete form.dataset.editId;

            document.querySelector("#productForm button").textContent =
                "Add Product";

            loadProducts();
        })
        .catch(error => console.error(error));
});
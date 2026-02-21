const MIN_LOADING_TIME = 2000; // milliseconds

function uploadFile() {

    const fileInput = document.getElementById("fileInput");
    const file = fileInput.files[0];
    const errorMessage = document.getElementById("errorMessage");
    const fileNameDisplay = document.getElementById("fileName");
    const loading = document.getElementById("loading");

    errorMessage.innerText = "";

    if (!file) {
        errorMessage.innerText = "Please select a CSV file.";
        return;
    }

    fileNameDisplay.innerText = "Selected File: " + file.name;

    const formData = new FormData();
    formData.append("file", file);

    // SHOW LOADER
    loading.classList.remove("hidden");

    const startTime = Date.now();

    fetch("/api/analytics/upload", {
        method: "POST",
        body: formData
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Server error " + response.status);
        }
        return response.json();
    })
    .then(data => {

        const elapsedTime = Date.now() - startTime;
        const remainingTime = Math.max(MIN_LOADING_TIME - elapsedTime, 0);

        setTimeout(() => {
            loading.classList.add("hidden");
            activateDashboard();
            displayResults(data);
        }, remainingTime);
    })
    .catch(error => {
        console.error(error);

        const elapsedTime = Date.now() - startTime;
        const remainingTime = Math.max(MIN_LOADING_TIME - elapsedTime, 0);

        setTimeout(() => {
            loading.classList.add("hidden");
            errorMessage.innerText = "Failed to process file. Check backend logs.";
        }, remainingTime);
    });
}

document.getElementById("fileInput").addEventListener("change", function () {
    const file = this.files[0];
    if (file) {
        document.getElementById("fileName").innerText =
            "Selected File: " + file.name;
    }
});

function activateDashboard() {
    document.getElementById("mainContent").classList.add("hidden");
    document.getElementById("dashboard").classList.remove("hidden");

    const header = document.getElementById("headerSection");
    header.classList.remove("large");
    header.classList.add("small");
}

function displayResults(data) {

    document.getElementById("totalIncome").innerText = formatCurrency(data.totalIncome);
    document.getElementById("totalExpense").innerText = formatCurrency(data.totalExpense);
    document.getElementById("netBalance").innerText = formatCurrency(data.netBalance);
    document.getElementById("averageTransaction").innerText = formatCurrency(data.averageTransaction);
    document.getElementById("topCategory").innerText = data.topSpendingCategory || "-";

    if (data.highestTransaction) {
        document.getElementById("highestExpense").innerText =
            formatCurrency(data.highestTransaction.amount) +
            " (" + data.highestTransaction.category + ")";
    }

    const categoryList = document.getElementById("categoryBreakdown");
    categoryList.innerHTML = "";

    Object.entries(data.categoryBreakdown || {}).forEach(([key, value]) => {
        const li = document.createElement("li");
        li.innerText = key + " : " + formatCurrency(value);
        categoryList.appendChild(li);
    });

    const monthlyList = document.getElementById("monthlyTrend");
    monthlyList.innerHTML = "";

    Object.entries(data.monthlyExpenseTrend || {}).forEach(([key, value]) => {
        const li = document.createElement("li");
        li.innerText = key + " : " + formatCurrency(value);
        monthlyList.appendChild(li);
    });
}

function formatCurrency(value) {
    if (value === null || value === undefined) return "-";
    return "₹ " + Number(value).toLocaleString("en-IN", {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    });
}
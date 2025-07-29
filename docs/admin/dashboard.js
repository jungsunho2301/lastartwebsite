// 로그인 상태 처리
const adminButton = document.getElementById("adminButton");
adminButton.addEventListener("click", () => {
  const isAdmin = localStorage.getItem("isAdmin") === "true";
  if (isAdmin) {
    localStorage.removeItem("isAdmin");
    location.reload();
  } else {
    location.href = "login.html";
  }
});

// 메뉴 버튼 이동 처리
const navLinks = document.querySelectorAll(".nav-links a");
navLinks.forEach((link) => {
  link.addEventListener("click", (e) => {
    e.preventDefault();
    const href = link.getAttribute("href");
    if (href === "index.html") {
      location.href = "../index.html";
    } else if (href.startsWith("http") || href.startsWith("/")) {
      location.href = href;
    } else {
      location.href = `../${href}`;
    }
  });
});

// 이메일 변경
emailForm.addEventListener("submit", async (e) => {
  e.preventDefault();
  const res = await fetch("/admin/api/email", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email: newEmail.value }),
  });
  alert(await res.text());
});

// SNS 연결
instagramForm.addEventListener("submit", async (e) => {
  e.preventDefault();
  const res = await fetch("/admin/api/social/instagram", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ url: instagramUrl.value }),
  });
  alert(await res.text());
});

facebookForm.addEventListener("submit", async (e) => {
  e.preventDefault();
  const res = await fetch("/admin/api/social/facebook", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ url: facebookUrl.value }),
  });
  alert(await res.text());
});

// 뉴스레터 전송
newsletterForm.addEventListener("submit", async (e) => {
  e.preventDefault();
  const res = await fetch("/admin/api/news/send", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      title: newsletterTitle.value,
      content: newsletterContent.value,
    }),
  });
  alert(await res.text());
});

// 아티스트 텍스트 수정/삭제
artistTextForm.addEventListener("submit", async (e) => {
  e.preventDefault();
  const res = await fetch(`/admin/api/artist-text/${artistTextId.value}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ content: artistTextContent.value }),
  });
  alert(await res.text());
});

async function deleteArtistText() {
  const res = await fetch(`/admin/api/artist-text/${artistTextId.value}`, {
    method: "DELETE",
  });
  alert(await res.text());
}

// 이미지 미리보기 (아트워크/아트샵)
function setImagePreview(inputEl, imgEl, placeholderEl) {
  inputEl.addEventListener("change", (event) => {
    const file = event.target.files[0];
    if (file && file.type.startsWith("image/")) {
      const reader = new FileReader();
      reader.onload = function (e) {
        imgEl.src = e.target.result;
        imgEl.style.display = "block";
        placeholderEl.style.display = "none";
      };
      reader.readAsDataURL(file);
    } else {
      imgEl.style.display = "none";
      placeholderEl.style.display = "block";
    }
  });
}

setImagePreview(
  document.getElementById("artworkFile"),
  document.getElementById("artworkPreviewImage"),
  document.getElementById("artworkPlaceholderText")
);

setImagePreview(
  document.getElementById("shopImage"),
  document.getElementById("shopPreviewImage"),
  document.getElementById("shopPlaceholderText")
);

// 아트샵 조회/등록/수정/삭제
btnSearch.addEventListener("click", async () => {
  const id = productId.value;
  const res = await fetch(`/admin/api/shop/${id}`);
  const data = await res.json();
  titleInput.value = data.title;
  descInput.value = data.description;
  priceInput.value = data.price;
  shopPreviewImage.src = data.image_url;
  shopPreviewImage.style.display = "block";
  shopPlaceholderText.style.display = "none";
});

btnRegister.addEventListener("click", async () => {
  const formData = new FormData();
  formData.append("image", shopImage.files[0]);
  formData.append("title", titleInput.value);
  formData.append("description", descInput.value);
  formData.append("price", priceInput.value);
  await fetch("/admin/api/shop/create", {
    method: "POST",
    body: formData,
  });
  alert("등록 완료");
});

btnUpdate.addEventListener("click", async () => {
  const formData = new FormData();
  formData.append("image", shopImage.files[0]);
  formData.append("title", titleInput.value);
  formData.append("description", descInput.value);
  formData.append("price", priceInput.value);
  await fetch(`/admin/api/shop/update/${productId.value}`, {
    method: "PUT",
    body: formData,
  });
  alert("수정 완료");
});

btnDelete.addEventListener("click", async () => {
  await fetch(`/admin/api/shop/delete/${productId.value}`, {
    method: "DELETE",
  });
  alert("삭제 완료");
});

btnClear.addEventListener("click", () => {
  productId.value = "";
  titleInput.value = "";
  descInput.value = "";
  priceInput.value = "";
  shopPreviewImage.src = "";
  shopPreviewImage.style.display = "none";
  shopPlaceholderText.style.display = "block";
  shopImage.value = "";
});

// dashboard.js - 관리자 기능 전체 통합 스크립트

// ✅ 로그인 버튼 → 로그인 페이지 이동
const adminButton = document.getElementById("adminButton");
adminButton?.addEventListener("click", () => {
  window.location.href = "login.html";
});

// ✅ 메뉴 이동 처리
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
emailForm?.addEventListener("submit", async (e) => {
  e.preventDefault();
  const res = await fetch("/admin/api/email", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email: newEmail.value }),
  });
  alert(await res.text());
});

// SNS 등록
instagramForm?.addEventListener("submit", async (e) => {
  e.preventDefault();
  const res = await fetch("/admin/api/social/instagram", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ url: instagramUrl.value }),
  });
  alert(await res.text());
});

facebookForm?.addEventListener("submit", async (e) => {
  e.preventDefault();
  const res = await fetch("/admin/api/social/facebook", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ url: facebookUrl.value }),
  });
  alert(await res.text());
});

// 뉴스레터 발송
newsletterForm?.addEventListener("submit", async (e) => {
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

// ✅ 이미지 미리보기
function setImagePreview(fileInput, imgElement, placeholder) {
  fileInput?.addEventListener("change", function (e) {
    const file = e.target.files[0];
    if (file) {
      imgElement.src = URL.createObjectURL(file);
      imgElement.style.display = "block";
      placeholder.style.display = "none";
    } else {
      imgElement.src = "";
      imgElement.style.display = "none";
      placeholder.style.display = "block";
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

// ✅ 아트워크 등록/수정
btnApply?.addEventListener("click", async () => {
  const artworkId = document.getElementById("artworkId").value.trim();
  const file = document.getElementById("artworkFile").files[0];
  if (!file) return alert("이미지를 선택해주세요");
  const formData = new FormData();
  formData.append("image", file);

  const url = artworkId
    ? `/admin/api/artwork/${artworkId}`
    : `/admin/api/artwork/upload`;
  const method = artworkId ? "PUT" : "POST";

  const res = await fetch(url, {
    method,
    body: formData,
    credentials: "include",
  });

  alert(res.ok ? "완료" : "실패");
});

// ✅ 아트워크 삭제
btnDelete?.addEventListener("click", async () => {
  const artworkId = document.getElementById("artworkId").value.trim();
  if (!artworkId) return alert("ID를 입력해주세요");
  if (!confirm(`정말 삭제하시겠습니까? ID: ${artworkId}`)) return;

  const res = await fetch(`/admin/api/artwork/${artworkId}`, {
    method: "DELETE",
    credentials: "include",
  });
  alert(res.ok ? "삭제 완료" : "삭제 실패");
});

// 아트샵 기능
btnSearch?.addEventListener("click", async () => {
  const id = productId.value;
  const res = await fetch(`/admin/api/artshop/${id}`);
  const data = await res.json();
  titleInput.value = data.title;
  descInput.value = data.description;
  priceInput.value = data.price;
  shopPreviewImage.src = data.image_url;
  shopPreviewImage.style.display = "block";
  shopPlaceholderText.style.display = "none";
});

btnRegister?.addEventListener("click", async () => {
  const formData = new FormData();
  formData.append("image", shopImage.files[0]);
  formData.append("title", titleInput.value);
  formData.append("description", descInput.value);
  formData.append("price", priceInput.value);

  await fetch("/admin/api/artshop/upload", {
    method: "POST",
    body: formData,
  });
  alert("등록 완료");
});

btnUpdate?.addEventListener("click", async () => {
  const formData = new FormData();
  formData.append("image", shopImage.files[0]);
  formData.append("title", titleInput.value);
  formData.append("description", descInput.value);
  formData.append("price", priceInput.value);

  await fetch(`/admin/api/artshop/${productId.value}`, {
    method: "PUT",
    body: formData,
  });
  alert("수정 완료");
});

btnDelete?.addEventListener("click", async () => {
  await fetch(`/admin/api/artshop/${productId.value}`, {
    method: "DELETE",
  });
  alert("삭제 완료");
});

// ✅ 아트샵 입력 초기화
btnClear?.addEventListener("click", () => {
  productId.value = "";
  titleInput.value = "";
  artistInput.value = "";
  descInput.value = "";
  priceInput.value = "";
  shopPreviewImage.src = "";
  shopPreviewImage.style.display = "none";
  shopPlaceholderText.style.display = "block";
  shopImage.value = "";
});

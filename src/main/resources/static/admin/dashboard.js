// dashboard.js 전체 수정 버전

// 로그인 버튼 → 로그인 페이지 이동
const adminButton = document.getElementById("adminButton");
adminButton?.addEventListener("click", () => {
  window.location.href = "login.html";
});

// 메뉴 이동 처리
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

// 아트워크 이미지 미리보기
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

// 아트워크 등록/수정
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

// 아트워크 삭제
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





btnClear?.addEventListener("click", () => {
  productId.value = "";
  titleInput.value = "";
  descInput.value = "";
  priceInput.value = "";
  shopPreviewImage.src = "";
  shopPreviewImage.style.display = "none";
  shopPlaceholderText.style.display = "block";
  shopImage.value = "";
});

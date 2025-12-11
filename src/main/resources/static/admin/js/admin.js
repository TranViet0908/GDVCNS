// Toast Notification Function
function showToast(message, type = "info") {
  const toastContainer = document.getElementById("toastContainer")
  if (!toastContainer) return

  const toastId = "toast-" + Date.now()
  const toastBgClass = getToastBgClass(type)
  const toastIcon = getToastIcon(type)

  const toastHTML = `
        <div class="toast show" id="${toastId}" role="alert">
            <div class="toast-body bg-${toastBgClass}">
                <i class="fas fa-${toastIcon} me-2"></i>
                <span>${message}</span>
                <button type="button" class="btn-close" data-bs-dismiss="toast"></button>
            </div>
        </div>
    `

  const toastElement = document.createElement("div")
  toastElement.innerHTML = toastHTML
  toastContainer.appendChild(toastElement.firstElementChild)

  const toast = document.getElementById(toastId)

  // Animate toast entry
  window.anime({
    targets: toast,
    opacity: [0, 1],
    translateX: [20, 0],
    duration: 300,
    easing: "easeOutQuad",
  })

  // Auto-remove toast after 3 seconds
  setTimeout(() => {
    window.anime({
      targets: toast,
      opacity: [1, 0],
      translateX: [0, 20],
      duration: 300,
      easing: "easeOutQuad",
      complete: () => toast.remove(),
    })
  }, 3000)
}

function getToastBgClass(type) {
  const types = {
    success: "success",
    error: "danger",
    danger: "danger",
    warning: "warning",
    info: "info",
  }
  return types[type] || "info"
}

function getToastIcon(type) {
  const icons = {
    success: "check-circle",
    error: "exclamation-circle",
    danger: "exclamation-circle",
    warning: "exclamation-triangle",
    info: "info-circle",
  }
  return icons[type] || "info-circle"
}

// Sidebar Toggle
function initSidebar() {
  const hamburgerBtn = document.getElementById("hamburgerBtn")
  const sidebarToggle = document.getElementById("sidebarToggle")
  const sidebar = document.querySelector(".sidebar")

  if (hamburgerBtn) {
    hamburgerBtn.addEventListener("click", toggleSidebar)
  }

  if (sidebarToggle) {
    sidebarToggle.addEventListener("click", toggleSidebar)
  }

  // Restore sidebar state from localStorage
  if (localStorage.getItem("sidebar-collapsed") === "true") {
    sidebar.classList.add("collapsed")
  }
}

function toggleSidebar() {
  const sidebar = document.querySelector(".sidebar")
  sidebar.classList.toggle("collapsed")
  localStorage.setItem("sidebar-collapsed", sidebar.classList.contains("collapsed"))

  // Animate sidebar toggle
  window.anime({
    targets: sidebar,
    duration: 300,
    easing: "easeInOutQuad",
  })
}

// Image Preview
function initImagePreview() {
  const fileInputs = document.querySelectorAll('input[type="file"]')

  fileInputs.forEach((input) => {
    input.addEventListener("change", function () {
      const previewId = this.id + "Preview"
      const preview = document.getElementById(previewId)

      if (preview && this.files && this.files[0]) {
        const reader = new FileReader()
        reader.onload = (e) => {
          preview.src = e.target.result

          // Animate preview
          window.anime({
            targets: preview,
            opacity: [0.5, 1],
            scale: [0.9, 1],
            duration: 300,
            easing: "easeOutQuad",
          })
        }
        reader.readAsDataURL(this.files[0])
      }
    })
  })
}

// Form Validation
function initFormValidation() {
  const forms = document.querySelectorAll("[novalidate]")

  forms.forEach((form) => {
    form.addEventListener("submit", function (e) {
      if (!this.checkValidity()) {
        e.preventDefault()
        e.stopPropagation()

        // Highlight invalid fields
        this.querySelectorAll(":invalid").forEach((field) => {
          field.classList.add("is-invalid")
          field.addEventListener("input", function () {
            if (this.checkValidity()) {
              this.classList.remove("is-invalid")
            }
          })
        })

        showToast("Vui lòng điền đầy đủ các trường bắt buộc", "warning")
      }
    })
  })
}

// Delete Confirmation
function confirmDelete(title = "Xác nhận xóa?", message = "Hành động này không thể hoàn tác!") {
  return confirm(`${title}\n${message}`)
}

function deleteWithModal(itemName = "mục này", onConfirm) {
  const modal = new window.bootstrap.Modal(document.getElementById("deleteModal"))
  const confirmBtn = document.querySelector(".delete-confirm-btn")

  if (confirmBtn) {
    confirmBtn.onclick = () => {
      if (onConfirm) {
        onConfirm()
      }
      modal.hide()
    }
  }

  modal.show()
}

// Initialize all features on page load
document.addEventListener("DOMContentLoaded", () => {
  initSidebar()
  initImagePreview()
  initFormValidation()
  initTableFeatures()
  initEditorPreview()
})

// Table Features
function initTableFeatures() {
  // Row hover effects
  const tableRows = document.querySelectorAll(".table tbody tr")
  tableRows.forEach((row) => {
    row.addEventListener("mouseenter", function () {
      window.anime({
        targets: this,
        backgroundColor: ["#f8f9fa", "#e9ecef"],
        duration: 200,
      })
    })

    row.addEventListener("mouseleave", function () {
      window.anime({
        targets: this,
        backgroundColor: ["#e9ecef", "#ffffff"],
        duration: 200,
      })
    })
  })

  // Checkbox functionality
  const checkboxes = document.querySelectorAll('input[type="checkbox"]')
  const selectAllCheckbox = document.querySelector(".select-all-checkbox")

  if (selectAllCheckbox) {
    selectAllCheckbox.addEventListener("change", function () {
      checkboxes.forEach((checkbox) => {
        if (checkbox !== this) {
          checkbox.checked = this.checked
        }
      })
    })
  }
}

// Editor Preview
function initEditorPreview() {
  const previewBtn = document.querySelector(".btn-preview")
  if (previewBtn) {
    previewBtn.addEventListener("click", () => {
      const editorContent =
        document.querySelector(".ck-content")?.innerHTML ||
        document.querySelector(".editor")?.value ||
        "Không có nội dung để xem trước"

      const modal = new window.bootstrap.Modal(document.getElementById("previewModal"))
      const previewContainer = document.querySelector(".preview-content")

      if (previewContainer) {
        previewContainer.innerHTML = editorContent
        modal.show()
      }
    })
  }
}

// Utility Functions
// Slugify text for URL-friendly strings
function slugify(text) {
  return text
    .toLowerCase()
    .trim()
    .replace(/[^\w\s-]/g, "")
    .replace(/[\s_-]+/g, "-")
    .replace(/^-+|-+$/g, "")
}

// Auto-generate slug from title
function autoGenerateSlug(titleInputId, slugInputId) {
  const titleInput = document.getElementById(titleInputId)
  const slugInput = document.getElementById(slugInputId)

  if (titleInput && slugInput && !slugInput.value) {
    titleInput.addEventListener("input", function () {
      slugInput.value = slugify(this.value)
    })
  }
}

// Format date to Vietnamese format
function formatDateVN(date) {
  const options = { year: "numeric", month: "long", day: "numeric" }
  return new Date(date).toLocaleDateString("vi-VN", options)
}

// Copy text to clipboard
function copyToClipboard(text) {
  navigator.clipboard.writeText(text).then(() => {
    showToast("Đã sao chép vào clipboard", "success")
  })
}

// Character counter for textarea
function initCharacterCounter(textareaId, counterId, maxChars = 500) {
  const textarea = document.getElementById(textareaId)
  const counter = document.getElementById(counterId)

  if (textarea && counter) {
    textarea.addEventListener("input", function () {
      const remaining = maxChars - this.value.length
      counter.textContent = `${this.value.length}/${maxChars}`

      if (remaining < 50) {
        counter.classList.add("text-warning")
      } else {
        counter.classList.remove("text-warning")
      }
    })
  }
}

// Disable submit button during form submission
document.addEventListener(
  "submit",
  (e) => {
    if (e.target.tagName === "FORM") {
      const submitBtn = e.target.querySelector('button[type="submit"]')
      if (submitBtn) {
        submitBtn.disabled = true
        const originalText = submitBtn.innerHTML
        submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Đang xử lý...'

        // Re-enable after a delay if needed (in case of error)
        setTimeout(() => {
          submitBtn.disabled = false
          submitBtn.innerHTML = originalText
        }, 10000)
      }
    }
  },
  true,
)

// Export functions for use in HTML
window.confirmDelete = confirmDelete
window.deleteWithModal = deleteWithModal
window.showToast = showToast
window.slugify = slugify
window.autoGenerateSlug = autoGenerateSlug
window.formatDateVN = formatDateVN
window.copyToClipboard = copyToClipboard
window.initCharacterCounter = initCharacterCounter

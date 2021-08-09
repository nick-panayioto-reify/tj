(defun tj-json-to-transit ()
  (interactive)
  (let* ((in-text (shell-quote-argument (buffer-string)))
	 (out-text (shell-command-to-string (concat "tj -i json -o transit " in-text)))
	 (out-buf (generate-new-buffer "tj-output")))
    (with-current-buffer out-buf
      (switch-to-buffer out-buf)
      (insert out-text))))

(defun tj-transit-to-json ()
  (interactive)
  (let* ((in-text (shell-quote-argument (buffer-string)))
	 (out-text (shell-command-to-string (concat "tj " in-text)))
	 (out-buf (generate-new-buffer "tj-output")))
    (with-current-buffer out-buf
      (insert out-text)
      (switch-to-buffer out-buf)
      (js-mode))))

(defun tj-json-to-edn ()
  (interactive)
  (let* ((in-text (shell-quote-argument (buffer-string)))
	 (out-text (shell-command-to-string (concat "tj -i json -o edn " in-text)))
	 (out-buf (generate-new-buffer "tj-output")))
    (with-current-buffer out-buf
      (switch-to-buffer out-buf)
      (insert out-text)
      (clojure-mode))))

(defun tj-edn-to-json ()
  (interactive)
  (let* ((in-text (shell-quote-argument (buffer-string)))
	 (out-text (shell-command-to-string (concat "tj -i edn -o json " in-text)))
	 (out-buf (generate-new-buffer "tj-output")))
    (with-current-buffer out-buf
      (switch-to-buffer out-buf)
      (insert out-text)
      (js-mode))))

(defun tj-transit-to-edn ()
  (interactive)
  (let* ((in-text (shell-quote-argument (buffer-string)))
	 (out-text (shell-command-to-string (concat "tj -i transit -o edn " in-text)))
	 (out-buf (generate-new-buffer "tj-output")))
    (with-current-buffer out-buf
      (switch-to-buffer out-buf)
      (insert out-text)
      (clojure-mode))))

(defun tj-edn-to-transit ()
  (interactive)
  (let* ((in-text (shell-quote-argument (buffer-string)))
	 (out-text (shell-command-to-string (concat "tj -i edn -o transit " in-text)))
	 (out-buf (generate-new-buffer "tj-output")))
    (with-current-buffer out-buf
      (switch-to-buffer out-buf)
      (insert out-text))))

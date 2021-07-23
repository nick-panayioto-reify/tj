(defun tj-convert-from-json ()
  (interactive)
  (let* ((in-text (buffer-string))
	(out-text (shell-command-to-string (concat "tj -j '" in-text "'"))) 
	(out-buf (generate-new-buffer "tj-output")))
    (with-current-buffer out-buf
      (switch-to-buffer out-buf)
      (insert out-text))))

(defun tj-convert-to-json ()
  (interactive)
  (let* ((in-text (buffer-string))
	(out-text (shell-command-to-string (concat "tj -t '" in-text "'"))) 
	(out-buf (generate-new-buffer "tj-output")))
    (with-current-buffer out-buf
			  (insert out-text)
			  (switch-to-buffer out-buf)
			  (js-mode))))

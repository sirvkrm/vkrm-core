/* Copyright (c) 2026 The Brave Authors. All rights reserved.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/. */

package org.chromium.chrome.browser.settings;

import android.app.Dialog;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import org.chromium.chrome.R;

import java.util.Locale;

/** Dialog fragment that lets the user pick a custom VKRM theme color from a draggable wheel. */
public class VkrmCustomColorPickerDialogFragment extends DialogFragment {
    public static final String REQUEST_KEY = "vkrm_custom_color_picker_request";
    public static final String RESULT_SELECTED_COLOR = "vkrm_custom_color_picker_result";

    private static final String ARG_INITIAL_COLOR = "initial_color";

    private int mSelectedColor;

    public static VkrmCustomColorPickerDialogFragment newInstance(@ColorInt int initialColor) {
        VkrmCustomColorPickerDialogFragment fragment = new VkrmCustomColorPickerDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INITIAL_COLOR, initialColor);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mSelectedColor = requireArguments().getInt(ARG_INITIAL_COLOR);

        View view =
                LayoutInflater.from(requireContext())
                        .inflate(R.layout.vkrm_custom_color_picker_dialog, null, false);
        VkrmColorWheelView colorWheel = view.findViewById(R.id.vkrm_color_wheel);
        View preview = view.findViewById(R.id.vkrm_color_preview);
        TextView hexValue = view.findViewById(R.id.vkrm_color_hex_value);

        colorWheel.setColor(mSelectedColor);
        colorWheel.setOnColorChangeListener(
                color -> {
                    mSelectedColor = color;
                    updatePreview(preview, hexValue, color);
                });
        updatePreview(preview, hexValue, mSelectedColor);

        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.vkrm_custom_color_title)
                .setView(view)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(
                        R.string.save,
                        (dialog, which) -> {
                            Bundle result = new Bundle();
                            result.putInt(RESULT_SELECTED_COLOR, mSelectedColor);
                            getParentFragmentManager().setFragmentResult(REQUEST_KEY, result);
                        })
                .create();
    }

    private void updatePreview(View preview, TextView hexValue, @ColorInt int color) {
        if (preview.getBackground() instanceof GradientDrawable drawable) {
            drawable.mutate();
            drawable.setColor(color);
        }
        hexValue.setText(String.format(Locale.US, "#%06X", 0xFFFFFF & color));
    }
}

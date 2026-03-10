/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/. */

export const images: NewTab.BraveBackground[] = []
// If you change the size of this array (e.g. adding a new background, adding a new property),
// then you must also update `script/generate_licenses.py`

export const updateImages = (newImages: NewTab.BraveBackground[]) => {
  if (!newImages.length) {
    // This can happen when the component for NTP is not downloaded yet on
    // a fresh profile.
    return
  }

  images.splice(0, images.length, ...newImages)
}

export const defaultSolidBackgroundColor = '#0077B6'

export const solidColorsForBackground: NewTab.ColorBackground[] = [
  '#0077B6', '#0096C7', '#00B4D8', '#48CAE4', '#90E0EF', '#ADE8F4', '#CAF0F8', '#5FA8D3',
  '#4EA8DE', '#5390D9', '#4361EE', '#3A0CA3', '#023E8A', '#1B4965', '#355070', '#0B1F33',
  '#03045E', '#001233', '#000814'
].map((color): NewTab.ColorBackground => ({ 'type': 'color', 'wallpaperColor': color }))

export const defaultGradientColor = 'linear-gradient(135deg, #CAF0F8 0%, #90E0EF 32%, #0077B6 70%, #023E8A 100%)'

export const gradientColorsForBackground: NewTab.ColorBackground[] = [
  'linear-gradient(135deg, #CAF0F8 0%, #90E0EF 32%, #0077B6 70%, #023E8A 100%)',
  'linear-gradient(135deg, #ADE8F4 0%, #48CAE4 35%, #0096C7 72%, #023E8A 100%)',
  'linear-gradient(135deg, #90E0EF 0%, #00B4D8 38%, #0077B6 72%, #03045E 100%)',
  'linear-gradient(145deg, #CAF0F8 0%, #5FA8D3 36%, #0077B6 68%, #001233 100%)',
  'radial-gradient(75% 75% at 82% 18%, #90E0EF 0%, #48CAE4 24%, #0077B6 65%, #03045E 100%)',
  'radial-gradient(80% 80% at 20% 18%, #CAF0F8 0%, #00B4D8 28%, #023E8A 72%, #000814 100%)',
  'linear-gradient(128deg, #ADE8F4 8%, #4EA8DE 42%, #023E8A 100%)',
  'linear-gradient(315deg, #90E0EF 12%, #00B4D8 42%, #0077B6 82%)',
  'linear-gradient(128deg, #CAF0F8 8%, #5FA8D3 48%, #355070 100%)',
  'linear-gradient(128deg, #48CAE4 8%, #0096C7 48%, #001233 100%)',
  'linear-gradient(130deg, #ADE8F4 10%, #0077B6 58%, #03045E 100%)'
].map((color): NewTab.ColorBackground => ({ 'type': 'color', 'wallpaperColor': color }))

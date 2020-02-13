#-*- coding:utf-8 -*-

from konlpy.tag import Twitter
twitter = Twitter()

try:
    import Image
except ImportError:
    from PIL import Image

import pytesseract
import os

reciept_kor = []

# 영어 인식
#reciept = (pytesseract.image_to_string(Image.open('test.png'))

# 한글 인식
reciept = (pytesseract.image_to_string(Image.open('test.png'), lang='Kor'))

#영수증 ocr_txt파일 한 문장씩 읽어서 저장
f = open("reciept.txt", 'w')
for r in reciept:
    f.write(r)
f.close()

#한 줄 씩 읽어서 리스트에 저장
f = open("reciept.txt", "r")
lines = f.readlines()

reciept_ = []
for line in lines:
    reciept_.append(line)
f.close()

#형태소 분석
sentences_tag = []
for sentence in reciept_:
    morph = twitter.pos(sentence)
    sentences_tag.append(morph)
    f = open("reciept_.txt", "w")
    for things in sentences_tag:
        f.write(str(things))
        f.write('\n')
    f.close()
        
"""
#명사_형용사 추출
noun_adj_list = []
for sentence1 in sentences_tag:
    for word, tag in sentence1:
        if tag in ['Noun','Adjective']:
            noun_adj_list.append(word)
"""
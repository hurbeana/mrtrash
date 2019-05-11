\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{mrtrash}[mrtrash class]

\DeclareOption*{\PassOptionsToClass{\CurrentOption}{scrreprt}} % Pass through any options to the base class
\ProcessOptions\relax % Process given options

\LoadClass[12pt, a4paper]{scrreprt} % Load the base class

%% Language and font encodings
\usepackage[ngerman]{babel}
\usepackage{helvet}
\renewcommand{\familydefault}{\sfdefault}
\usepackage{bibgerm}

%% Sets page size and margins
\usepackage[a4paper,top=2.5cm,bottom=2cm,left=2cm,right=2cm]{geometry}

%% Useful packages
\usepackage{amsmath}
\usepackage{graphicx}
\usepackage[colorlinks=true, allcolors=blue]{hyperref}
\usepackage{listings}
\usepackage{subcaption}
\usepackage{xcolor}
\usepackage{xhfill}

\lstset{
    basicstyle=\linespread{1.1}\ttfamily\footnotesize,
    breaklines=true,                 
    numbers=left,                    
    numbersep=5pt,                  
    showspaces=false,                
    showstringspaces=false,
    showtabs=false,                  
    tabsize=2,
    extendedchars=true,
    literate={ä}{{\"a}}1 {ö}{{\"o}}1 {ü}{{\"u}}1 {Ä}{{\"A}}1 {Ö}{{\"O}}1 {Ü}{{\"U}}1 {ß}{{\ss}}1 {€}{{\EUR}}1
}

%% tcolorbox
\usepackage{tcolorbox}
\definecolor{mycolor}{rgb}{0.122, 0.435, 0.698}% Rule colour

\setlength{\parindent}{0em}
\setlength{\parskip}{1em}

\usepackage{scrlayer-scrpage}
\KOMAoptions{
  headwidth=\textwidth+30pt:-15pt,
  headsepline=.2pt
}
%\clearpairofpagestyles
\chead{TU Wien, MSE}
\ohead{Hurbean, Schweiger [Gruppe 06]}
\renewcommand\chapterpagestyle{scrheadings}

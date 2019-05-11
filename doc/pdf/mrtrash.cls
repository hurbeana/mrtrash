\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{mrtrash}[mrtrash class]

\DeclareOption*{\PassOptionsToClass{\CurrentOption}{scrreprt}} % Pass through any options to the base class
\ProcessOptions\relax % Process given options

\LoadClass[12pt, a4paper]{scrreprt} % Load the base class

%% Language and font encodings
\RequirePackage[ngerman]{babel}
\RequirePackage[T1]{fontenc}
\RequirePackage{helvet}
\renewcommand{\familydefault}{\sfdefault}
\RequirePackage{bibgerm}

%% Sets page size and margins
\usepackage[a4paper,top=2.5cm,bottom=2cm,left=2cm,right=2cm]{geometry}

%% Useful packages
\RequirePackage{graphicx}
\RequirePackage[colorlinks=true, allcolors=blue]{hyperref}
\RequirePackage{listings}
\RequirePackage{subcaption}
\RequirePackage{xcolor}
\RequirePackage{xhfill}

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
\RequirePackage{tcolorbox}
\definecolor{mycolor}{rgb}{0.122, 0.435, 0.698}% Rule colour

\setlength{\parindent}{0em}
\setlength{\parskip}{1em}


\RequirePackage{scrlayer-scrpage}
\KOMAoptions{
  headwidth=\textwidth+30pt:-15pt,
  headsepline=.2pt
}
%\clearpairofpagestyles
\chead{TU Wien, MSE}
\ohead{Hurbean, Schweiger [Gruppe 06]}
\renewcommand\chapterpagestyle{scrheadings}

\RedeclareSectionCommand[beforeskip=0pt,
afterskip=0.5em]{chapter}
\RedeclareSectionCommand[beforeskip=1em,
afterskip=0.5em]{section}
\RedeclareSectionCommand[beforeskip=1em,
afterskip=0.5em]{subsection}

\newcommand{\lorem}{Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec a diam lectus.} % Dummy text

%----------------------------------------------------------------------------------------
%	GRAPHICS DEFINITIONS
%---------------------------------------------------------------------------------------- 

\usepackage{tikz} % Required for creating the plots
\usetikzlibrary{shapes, backgrounds}
\tikzset{x=1cm, y=1cm} % Default tikz units

% Command to vertically centre adjacent content
\newcommand{\vcenteredhbox}[1]{% The only parameter is for the content to centre
	\begingroup%
		\setbox0=\hbox{#1}\parbox{\wd0}{\box0}%
	\endgroup%
}

%----------------------------------------------------------------------------------------

%------------------------------------------------
% CHARTS
%------------------------------------------------

\newcounter{a}
\newcounter{b}
\newcounter{c}

% Command to output a number of automatically-sized bubbles from a string in the format of '<size>/<label>', e.g. \bubbles{5/Eclipse, 6/git, 4/Office, 3/Inkscape, 3/Blender}
\newcommand{\bubbles}[1]{
	% Reset counters
	\setcounter{a}{0}
	\setcounter{c}{150}
	
	\begin{tikzpicture}[scale=3]
		\foreach \p/\t in {#1} {
			\addtocounter{a}{1}
			\bubble{\thea/2}{\theb}{\p/25}{\t}{1\p0}
		}
	\end{tikzpicture}
}

% Command to output a bubble at a specific position with a specific size
\newcommand{\bubble}[5]{
	\filldraw[fill=black, draw=none] (#1,0.5) circle (#3); % Bubble
	\node[label=\textcolor{black}{#4}] at (#1,0.7) {}; % Label
}

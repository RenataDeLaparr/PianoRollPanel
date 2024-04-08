package seq.notes; // folder name

/**
 * A JPanel with a piano-roll representation on it :)
 */
public class PianoRollPanel extends seq.struc.LignePanel implements Piste.PisteListener {
  /**
   * Base Functionality
   */
  /**
   * constructor
   */
  public PianoRollPanel() {
    setBackground(java.awt.SystemColor.window);
    addMouseListener(new java.awt.event.MouseListener() {
      public void mousePressed(final java.awt.event.MouseEvent e) {
        PianoRollPanel.this.mousePressed(e);
      }
      public void mouseReleased(final java.awt.event.MouseEvent e) {
        PianoRollPanel.this.mouseReleased(e);
      }
      public void mouseClicked(final java.awt.event.MouseEvent e) {
        PianoRollPanel.this.mouseClicked(e);
      }
      public void mouseExited(final java.awt.event.MouseEvent e) {
        PianoRollPanel.this.mouseExited(e);
      }
      public void mouseEntered(final java.awt.event.MouseEvent e) {
        PianoRollPanel.this.mouseEntered(e);
      }
    });
    addMouseMotionListener(new java.awt.event.MouseMotionListener() {
      public void mouseDragged(final java.awt.event.MouseEvent e) {
        PianoRollPanel.this.mouseDragged(e);
      }
      public void mouseMoved(final java.awt.event.MouseEvent e) {
        PianoRollPanel.this.mouseMoved(e);
      }
    });
    addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(java.awt.event.KeyEvent e) {PianoRollPanel.this.keyPressed(e);}
    });
    new java.awt.dnd.DropTarget(this, new java.awt.dnd.DropTargetListener() {
      public void drop(final java.awt.dnd.DropTargetDropEvent e) {
        boolean result=false;
        if(e.isDataFlavorSupported(seq.struc.Ligne.LIGNES_FLAVOR)) {
          try {
            java.util.Vector ls=(java.util.Vector)e.getTransferable().getTransferData(seq.struc.Ligne.LIGNES_FLAVOR);
            e.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY);
            for(int i=ls.size()-1;i>=0;--i)
              if(ls.elementAt(i) instanceof Piste) {
                result=true;
                addPiste((Piste)ls.elementAt(i));
              }
          } catch(Exception x) {
            x.printStackTrace();
            e.rejectDrop();
          }
        } else
          e.rejectDrop();
        e.dropComplete(result);
      }
      public void dragEnter(final java.awt.dnd.DropTargetDragEvent e) {
        //nothing
      }
      public void dragExit(final java.awt.dnd.DropTargetEvent e) {
        //nothing
      }
      public void dragOver(final java.awt.dnd.DropTargetDragEvent e) {
        //nothing
      }
      public void dropActionChanged(final java.awt.dnd.DropTargetDragEvent e) {
        //nothing
      }
    });
    clavier_.setOpaque(true);
    clavier_.setBackground(java.awt.Color.white);
    clavier_.setForeground(java.awt.Color.black);
    clavier_.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 9));
    java.awt.Dimension d=new java.awt.Dimension(40, 128*getPixParH());
    clavier_.setPreferredSize(d);
    clavier_.setSize(d);
    clavier_.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent e) {
        if(getPisteCount()>0) {
          Piste p=getPisteAt(0);
          seq.jouer.Transporteur.joue(p.getCanal()==null ? 0 : p.getCanal().numero,p.getVolumeOrig(),p.getPanOrig(),p.getInstrument()==null ? 0 : p.getInstrument().prg,yToH(e.getY()),100,seq.mesure.Mesure.TICS_PAR_DEFAUT/2);
        } else
          seq.jouer.Transporteur.joue(0,-1,-1,0,yToH(e.getY()),100,seq.mesure.Mesure.TICS_PAR_DEFAUT/2);
      }
      public void mouseEntered(java.awt.event.MouseEvent e) {
        regleDoigt(yToH(e.getY()));
      }
    });
    clavier_.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
      public void mouseMoved(java.awt.event.MouseEvent e) {
        regleDoigt(yToH(e.getY()));
      }
    });
    west_.setView(clavier_);
    west_.setMinimumSize(new java.awt.Dimension(40,0));
    west_.setMaximumSize(new java.awt.Dimension(40, Integer.MAX_VALUE));
    west_.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(final javax.swing.event.ChangeEvent e) {
        ajusteScrollVertical();
      }
    });
    scrollVertical_.addAdjustmentListener(new java.awt.event.AdjustmentListener() {
      public void adjustmentValueChanged(final java.awt.event.AdjustmentEvent e) {
        placeHaut();
      }
    });
    east_.add(scrollVertical_, java.awt.BorderLayout.WEST);
    javax.swing.JPanel p=new javax.swing.JPanel(new java.awt.GridLayout(0,1));
    p.add(new seq.ui.SButton("+", seq.ui.SButton.MARGE_VIDE) {
      protected void action() {
        setPixParH(getPixParH()+1);
      }
    });
    p.add(new seq.ui.SButton("-", seq.ui.SButton.MARGE_VIDE) {
      protected void action() {
        setPixParH(getPixParH()-1);
      }
    });
    east_.add(p, java.awt.BorderLayout.CENTER);
    north_.setLayout(new javax.swing.BoxLayout(north_, javax.swing.BoxLayout.X_AXIS));
    javax.swing.ButtonGroup g=new javax.swing.ButtonGroup();
    javax.swing.JRadioButton rb=new javax.swing.JRadioButton("Write", !getRubberband());
    g.add(rb);
    north_.add(rb);
    final javax.swing.JRadioButton rbsel=new javax.swing.JRadioButton("Select", getRubberband());
    g.add(rbsel);
    north_.add(rbsel);
    java.awt.event.ActionListener rub=new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent e) {setRubberband(rbsel.isSelected()); PianoRollPanel.this.requestFocus();}
    };
    rbsel.addActionListener(rub);
    rb.addActionListener(rub);
    panelPistes_.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,2,0,4));
    north_.add(panelPistes_);
    north_.add(quantifieurDebuts_);
    quantifieurDebuts_.setMaximumSize(quantifieurDebuts_.getPreferredSize());
    north_.add(quantifieurFins_);
    quantifieurFins_.setMaximumSize(quantifieurFins_.getPreferredSize());
    north_.add(quantVelos_);
    north_.add(javax.swing.Box.createHorizontalGlue());
  }
  /**
   * Adjusts the preferredSize and revalidate
   */
  public void adapte() {
    super.adapte();
    java.awt.Dimension dc=new java.awt.Dimension(40,128*getPixParH());
    clavier_.setPreferredSize(dc);
    clavier_.setSize(dc);
    ajusteScrollVertical();
    clavier_.validate();
    clavier_.repaint();
  }
  /**
   * Draws the given portion of <B>this</B>
   */
  protected void dessine(final java.awt.Graphics2D g, final int minx, final int maxx, final int mintic, final int maxtic) {
    dessineClavier(g, minx, maxx);
    dessineGrille(g, minx, maxx);
    removeAllNotesDessinees();
    for(int i=getPisteCount()-1;i>=0;--i) {
      seq.notes.Piste p=getPisteAt(i);
      java.awt.Color tour=i==0 ? p.getCouleur() : p.getCouleur().palie,
                     centre=p.getCouleur().palie;
      boolean done=false;
      for(java.util.Iterator ns=p.getFinsApres(mintic);!done && ns.hasNext();) {
        seq.notes.Note n=(seq.notes.Note)ns.next();
        if(n.getDebut()>maxtic)
          done=true;
        else
          makeNoteDessinee(p,n).dessine(g,tour,estSelectionnee(n) ? p.getCouleur() : centre);
      }
    }
  }
  /**
   * Draws horizontal lines to identify note heights
   */
  protected void dessineClavier(final java.awt.Graphics2D g, final int minx, final int maxx) {
    for(int i=0;i<=128;++i)
      g.drawLine(minx,pixParH_*i,maxx,pixParH_*i);
  }
  /**
   * Removes the given lines from <B>this</B>
   */
  public void nettoie() {
    while(getPisteCount()>0)
      removePiste(getPisteAt(getPisteCount()-1));
    setMetrique(null);
  }
  /**
   * Calculates the preferred height of <B>this</B>
   */
  protected int calcPreferredHeight() {
    return 128*pixParH_;
  }
  /**
   * Tracks
   */
  private final javax.swing.DefaultComboBoxModel pistes_=new javax.swing.DefaultComboBoxModel();
  /**
   * Adds a Track
   */
  public void addPiste(final Piste piste) {
    if(pistes_.getIndexOf(piste) >= 0)
      return;
    pistes_.insertElementAt(piste, 0);
    // Additional logic to integrate the track into the UI
  }
  /**
   * Removes a Track
   */
  public void removePiste(final Piste piste) {
    int i=pistes_.getIndexOf(piste);
    if(i >= 0) {
      // Logic to remove the track from the UI and cleanup
    }
  }
  /**
   * Gets the number of Tracks
   */
  public int getPisteCount() {
    return pistes_.getSize();
  }
  /**
   * Returns the Track at given index
   */
  public Piste getPisteAt(final int index) {
    return (seq.notes.Piste)pistes_.getElementAt(index);
  }
  /**
   * Sets the Track to the top
   */
  public void toTop(final Piste p) {
    // Logic to bring a track to the forefront
  }
  /**
   * Gets the Tracks
   */
  public javax.swing.ComboBoxModel getPistes() {
    return pistes_;
  }
  private final javax.swing.JPanel panelPistes_=new javax.swing.JPanel(new java.awt.GridLayout(1,0));
  /**
   * A button to select the active Track
   */
  protected class BoutonAPiste extends javax.swing.JLabel {
    /**
     * Base Functionality
     */
    /**
     * constructor
     */
    public BoutonAPiste(final Piste piste) {
      super();
      this.piste = piste;
      // Setup logic for the track button
    }
    /**  */
    public final Piste piste;
    /**  */
    public void actualise() {
      // Logic to update the button's appearance based on its state
    }
  }
  /**
   * Metric
   */
  /**
   * Sets the Metric
   */
  public void setMetrique(final seq.mesure.Metrique metrique) {
    // Logic to set the metric and update the UI accordingly
  }
  /**
   * Draws the measure grid
   */
  protected void dessineGrille(final java.awt.Graphics2D g, final int minx, final int maxx) {
    // Logic to draw the measure grid on the UI
  }
  public final static java.awt.Color barreDeTemps=java.awt.Color.lightGray;
  /**
   * Returns true if <B>this</B> needs a Metric to display
   */
  public boolean veutMetrique() {
    return true;
  }
  /**
   * LineListener
   */
  /**
   * Called by the given Line when its name changes, if it has been added to it by the default LineListener
   */
  protected void nomAChange(final seq.struc.Ligne l) {
    // Logic to handle name changes of lines
  }
  /**
   * Called by the given Line when it is going to be destroyed, if it has been added by the default LineListener
   * Do not forget to remove it
   * Can call nouvelleEtendueVoulue(nmin,nmax)
   */
  protected void ligneVaEtreOtee(final seq.struc.Ligne l) {
    // Logic to handle when a line is about to be removed
  }
  /**
   * Called by the given Line when its extent has changed, if it has been added by the default LineListener
   * Can call nouvelleEtendueVoulue(nmin,nmax)
   */
  protected void etendueAChange(final seq.struc.Ligne l) {
    // Logic to handle extent changes of lines
  }
  /**
   * Recalculates the desired extent and calls nouvelleEtendueVoulue() and adapte()
   */
  protected void regleEtendueVoulue() {
    // Logic to recalculate and adjust the desired extent
  }
  /**
   * TrackListener
   */
  /**
   * Called by the given track when its channel changes
   */
  public void canalAChange(final Piste p) {
    // Logic to handle channel changes in a track
  }
  /**
   * Called by the given track when its instrument changes
   */
  public void instrumentAChange(final Piste p) {
    // Logic to handle instrument changes in a track
  }
  /**
   * Called by the given track when its color changes
   */
  public void couleurAChange(final Piste p) {
    // Logic to handle color changes in a track
  }
  /**
   * Called by the given track when the given Note changes
   */
  public void noteAChange(final Piste p, final Note n) {
    // Logic to handle note changes in a track
  }
  /**
   * Called by the given track when the given Note has been added
   */
  public void noteAjoutee(final Piste p, final Note n) {
    // Logic to handle the addition of notes in a track
  }
  /**
   * Called by the given track before the given Note is removed
   */
  public void noteVaEtreOtee(final Piste p, final Note n) {
    // Logic to handle the removal of notes from a track
  }
  /**
   * Called by the given Track when its original pan changes
   */
  public void panOrigAChange(final Piste p) {
    // Logic to handle changes in a track's original pan
  }
  /**
   * Called by the given Track when its original volume changes
   */
  public void volumeOrigAChange(final Piste p) {
    // Logic to handle changes in a track's original volume
  }
  /**
   * MetricListener
   */
  /**
   * Called by the given Metric when the Measure changes for the given date, provided it has been added as a MetricListener
   * Possibly, an indication present at the given date has just been removed
   */
  protected void mesureAChange(final seq.mesure.Metrique m, final Integer quand) {
    // Logic to handle measure changes in the metric
  }
  /**
   * PixPerH
   */
  private int pixParH_=5;
  /**
   * Gets the PixPerH
   */
  public int getPixParH() {
    return pixParH_;
  }
  /**
   * Sets the PixPerH
   */
  public void setPixParH(final int pixParH) {
    pixParH_=Math.max(3, pixParH);
    adapte();
  }
  /**
   * Notes Drawn
   */
  protected class NoteDessinee extends java.awt.Rectangle {
    public Piste p;
    public Note n;
    public void init(final Piste p, final Note n) {
      this.p=p;
      this.n=n;
      // Initialization logic
    }
    public void dessine(final java.awt.Graphics2D g, final java.awt.Color tour, final java.awt.Color centre) {
      // Drawing logic for a note
    }
  }
  /**
   * Mouse
   */
  protected void mouseClicked(final java.awt.event.MouseEvent e) {
    // Logic to handle mouse clicks
  }
  protected void mouseEntered(final java.awt.event.MouseEvent e) {
    // Logic to handle mouse entering the component
  }
  protected void mouseMoved(final java.awt.event.MouseEvent e) {
    // Logic to handle mouse movement
  }
  protected void mouseExited(final java.awt.event.MouseEvent e) {
    // Logic to handle mouse exiting the component
  }
  protected void mousePressed(final java.awt.event.MouseEvent e) {
    // Logic to handle mouse button presses
  }
  protected void mouseDragged(final java.awt.event.MouseEvent e) {
    // Logic to handle mouse dragging
  }
  protected void mouseReleased(final java.awt.event.MouseEvent e) {
    // Logic to handle mouse button release
  }
  protected void regleCurseur(final int x, final int y) {
    // Logic to adjust the cursor
  }
  /**
   * Rubberband
   */
  private boolean rubberband_=false;
  /**
   * Gets the Rubberband
   */
  public boolean getRubberband() {
    return rubberband_;
  }
  /**
   * Sets the Rubberband
   */
  public void setRubberband(final boolean rubberband) {
    rubberband_=rubberband;
  }
  /**
   * Selection
   */
  public static class NoteSelectionnee {
    public Piste p;
    public Note n;
    public void update() {
      // Update logic for a selected note
    }
  }
  public boolean estSelectionnee(final Note n) {
    // Check if a note is selected
    return false;
  }
  protected void selectionne(final NoteDessinee nd) {
    // Logic to select a drawn note
  }
  public void selectionne(final Piste p, final Note n) {
    // Logic to select a note
  }
  protected void deselectionne(final NoteDessinee nd) {
    // Logic to deselect a drawn note
  }
  public void deselectionne(final Note n) {
    // Logic to deselect a note
  }
  public void clearSelection() {
    // Logic to clear the selection
  }
  public java.util.Enumeration getSelection() {
    // Logic to get the current selection
    return null;
  }
  /**
   * Copy the selection
   */
  public void copy() {
    // Logic to copy the selected notes
  }
  /**
   * Cut the selection
   */
  public void cut() {
    // Logic to cut the selected notes
  }
  /**
   * Delete the selection
   */
  public void delete() {
    // Logic to delete the selected notes
  }
  /**
   * Paste to the top Track
   */
  public void pasteUne() {
    // Logic to paste notes to the top track
  }
  /**
   * Paste to the top Track at the given location
   */
  public void pasteUne(final int ou) {
    // Logic to paste notes to the top track at a specific location
  }
  /**
   * Paste to the original Tracks
   */
  public void pasteOrig() {
    // Logic to paste notes back to their original tracks
  }
  /**
   * Paste to the original Tracks at the given location
   */
  public void pasteOrig(final int ou) {
    // Logic to paste notes back to their original tracks at a specific location
  }
  /**
   * Paste at best: into the current Track if all copied notes come from a single Track, otherwise to the original Tracks; at the cursor if there is one, otherwise in place
   */
  public void pasteAuMieux() {
    // Logic to paste notes in the most suitable way
  }
  /**
   * Key
   */
  protected void keyPressed(final java.awt.event.KeyEvent e) {
    // Logic to handle key presses
  }
  public boolean isFocusTraversable() {
    return true;
  }
  /**
   * Quantization
   */
  protected seq.mesure.Quantifieur getQuantifieurDebuts() {
    // Get the Quantifier for starts
    return null;
  }
  protected seq.mesure.Quantifieur getQuantifieurFins() {
    // Get the Quantifier for ends
    return null;
  }
  protected boolean quantifieVelos() {
    // Returns true if velocities should be quantized
    return false;
  }
  /**
   * Components
   */
  public boolean estRedimensionnable() {
    // Returns true if the component should be resizable vertically
    return true;
  }
  public javax.swing.JComponent getWestComponent() {
    // Get the component to place to the left of this in ScrollPanes, or null if there isn't one
    return null;
  }
  public javax.swing.JComponent getEastComponent() {
    // Get the component to place to the right of this in ScrollPanes, or null if there isn't one
    return null;
  }
  private int hDoigt_=-1;
  private final javax.swing.JPanel clavier_=new javax.swing.JPanel() {
    protected void paintComponent(java.awt.Graphics g) {
      // Custom painting code for the keyboard
    }
  };
  private final javax.swing.JViewport west_=new javax.swing.JViewport();
  protected void regleDoigt(final int nh) {
    // Adjust the finger on the keyboard
  }
  private final javax.swing.JScrollBar scrollVertical_=new javax.swing.JScrollBar();
  private final javax.swing.JPanel east_=new javax.swing.JPanel(new java.awt.BorderLayout());
  protected void ajusteScrollVertical() {
    // Adjust the vertical scrollbar
  }
  protected void placeHaut() {
    // Place the top of the scroll
  }
  private final javax.swing.JPanel north_=new javax.swing.JPanel();
  private final javax.swing.JComboBox quantifieurDebuts_=new javax.swing.JComboBox(seq.mesure.Quantifieur.QUANTIFIEURS_CONNUS);
  private final javax.swing.JComboBox quantifieurFins_=new javax.swing.JComboBox(seq.mesure.Quantifieur.QUANTIFIEURS_CONNUS);
  private final javax.swing.JCheckBox quantVelos_=new javax.swing.JCheckBox("Quant. Velo.", true);
  public javax.swing.JComponent getNorthComponent() {
    // Get the component to place above this in ScrollPanes, or null if there isn't one
    return null;
  }
  public javax.swing.JComponent getSouthComponent() {
    // Get the component to place below this in ScrollPanes, or null if there isn't one
    return null;
  }
  public boolean utiliseDrop() {
    // Returns true if this handles drops itself
    return true;
  }
  /**
   * VeloPanel
   */
  private VeloPanel veloPanel_=null;
  public VeloPanel getVeloPanel() {
    // Get the VeloPanel
    return veloPanel_;
  }
  public void setVeloPanel(final VeloPanel veloPanel) {
    // Set the VeloPanel
    this.veloPanel_ = veloPanel;
  }
  /**
   * Fichable
   */
  public String getEntryPref() {
    // Returns the preferred prefix for an entry for this
    return "fenetres/panels/pianoroll";
  }
  public void ecris(final seq.Fichable.Sauveur s) {
    // Called by the given Savior to tell this to write itself
  }
  public static PianoRollPanel makePianoRollPanel() {
    // Factory to create an empty PianoRollPanel
    return new PianoRollPanel();
  }
  public Object getFactory() {
    // Returns the factory for this, either a class or a Fichable
    return PianoRollPanel.class;
  }
  public java.lang.reflect.Method getFactoryMethod() {
    // Returns a method capable of creating this
    return null;
  }
  public Object[] getFactoryArgs() {
    // Returns the values of the arguments for the factory method
    return null;
  }
  public void lis(final seq.Fichable.Chargeur c) throws java.io.IOException {
    // Reads this from the given Loader
  }
  /**
   * Popup
   */
  protected void popup(final java.awt.event.MouseEvent e) {
    // Logic to display a popup menu
  }
  protected static PianoRollPanel popuped__=null;
  protected final static javax.swing.JPopupMenu popupMenu__=new javax.swing.JPopupMenu();
  protected final static seq.ui.SMenuItem copy__=new seq.ui.SMenuItem("Copy") {
    protected void action() {
      // Action logic for copy
    }
  };
  protected final static seq.ui.SMenuItem cut__=new seq.ui.SMenuItem("Cut") {
    protected void action() {
      // Action logic for cut
    }
  };
  protected final static javax.swing.JMenu pasteUne__=new javax.swing.JMenu("Paste towards ");
  protected final static seq.ui.SMenuItem pasteUneEnPlace__=new seq.ui.SMenuItem("In place") {
    protected void action() {
      // Action logic for paste in place
    }
  };
  protected final static seq.ui.SMenuItem pasteUneAuCurseur__=new seq.ui.SMenuItem("At the cursor") {
    protected void action() {
      // Action logic for paste at the cursor
    }
  };
  protected final static javax.swing.JMenu pasteOrig__=new javax.swing.JMenu("Paste");
  protected final static seq.ui.SMenuItem pasteOrigEnPlace__=new seq.ui.SMenuItem("In place") {
    protected void action() {
      // Action logic for paste in place (original)
    }
  };
  protected final static seq.ui.SMenuItem pasteOrigAuCurseur__=new seq.ui.SMenuItem("At the cursor") {
    protected void action() {
      // Action logic for paste at the cursor (original)
    }
  };
  protected static int pasteOu__=0;
  static {
    // Initialization logic for the popup menu
  }
}

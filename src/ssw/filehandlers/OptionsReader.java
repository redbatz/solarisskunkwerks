/*
Copyright (c) 2008~2009, Justin R. Bengtson (poopshotgun@yahoo.com)
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice,
        this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright notice,
        this list of conditions and the following disclaimer in the
        documentation and/or other materials provided with the distribution.
    * Neither the name of Justin R. Bengtson nor the names of contributors may
        be used to endorse or promote products derived from this software
        without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package ssw.filehandlers;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.StringTokenizer;
import ssw.Options;
import ssw.Constants;

public class OptionsReader {
    // provides a means of reading and writing options from a file.
    private Hashtable<String, String> HTOptions = new Hashtable<String, String>();

    public void ReadOptions( String filename, Options o ) throws IOException {
        // reads options into the specified class
        try {
            BufferedReader FR = new BufferedReader( new FileReader( filename ) );

            boolean EOF = false;
            String read = "";
            while( EOF == false ) {
                try {
                    read = FR.readLine();
                    if( read == null ) {
                        // We've hit the end of the file.
                        EOF = true;
                    } else {
                        if( read.equals( "EOF" ) ) {
                            // end of file.
                            EOF = true;
                        } else {
                            ProcessString( read );
                        }
                    }
                } catch (IOException e ) {
                    // probably just reached the end of the file
                    System.out.println( "had an ioexception reading options:\n" + read + "\n\n" );
                    EOF = true;
                }
            }

            FR.close();
        } catch ( FileNotFoundException f ) {
            // could not access the file.  use the defaults.
            // since the options table is already set to defaults, this does nothing
            File file = new File( Constants.OptionsFileName );
    
            // Create file if it does not exist
            file.createNewFile();
            return;
        }

        // ensure we have the correct hash table.  if not, use the defaults
        // and rewrte the file.
        SetOptions( o );
        WriteOptions( Constants.OptionsFileName, o );
    }

    public void WriteOptions( String filename, Options o ) throws IOException {
        // attempts to write the given options to a file.
        BufferedWriter FR = new BufferedWriter( new FileWriter( filename ) );
        FR.write( "########## Preferences file for " + Constants.AppName + " ##########" );
        FR.newLine();
        FR.write( "# DO NOT EDIT THIS FILE!" );
        FR.newLine();
        FR.newLine();
        FR.write( "########## Heat Options ##########" );
        FR.newLine();
        FR.write( "REMOVE_OS=" + GetBoolean( o.Heat_RemoveOSWeapons ) );
        FR.newLine();
        FR.write( "REMOVE_REAR=" + GetBoolean( o.Heat_RemoveOSWeapons ) );
        FR.newLine();
        FR.write( "UAC_RAC_FULLRATE=" + GetBoolean( o.Heat_UAC_RAC_FullRate ) );
        FR.newLine();
        FR.write( "REMOVE_EQUIPMENT=" + GetBoolean( o.Heat_RemoveEquipment ) );
        FR.newLine();
        FR.write( "REMOVE_JUMP=" + GetBoolean( o.Heat_RemoveJumps) );
        FR.newLine();
        if( o.Heat_RemoveJumps ) {
            FR.write( "REMOVE_RUN=" + GetBoolean( o.Heat_RemoveMovement ) );
        } else {
            FR.write( "REMOVE_RUN=FALSE" );
        }
        FR.newLine();
        FR.write( "REMOVE_STEALTH=" + GetBoolean( o.Heat_RemoveStealthArmor ) );
        FR.newLine();

        FR.newLine();
        FR.write( "########## Armor Options ##########" );
        FR.newLine();
        FR.write( "ARMOR_USE_CUSTOM=" + GetBoolean( o.Armor_CustomPercentage ) );
        FR.newLine();
        if( o.Armor_CustomPercentage ) {
            FR.write( "ARMOR_CTR=" + o.Armor_CTRPercent );
            FR.newLine();
            FR.write( "ARMOR_STR=" + o.Armor_STRPercent );
        } else {
            FR.write( "ARMOR_CTR=25" );
            FR.newLine();
            FR.write( "ARMOR_STR=25" );
        }
        FR.newLine();
        FR.write( "ARMOR_HEAD_ALLOC=" + o.Armor_Head );
        FR.newLine();
        FR.write( "ARMOR_PRIORITY=" + o.Armor_Priority );
        FR.newLine();

        FR.newLine();
        FR.write( "########## Equipment Options ##########" );
        FR.newLine();
        FR.write( "DEFAULT_RULES=" + o.DefaultRules );
        FR.newLine();
        FR.write( "DEFAULT_ERA=" + o.DefaultEra );
        FR.newLine();
        FR.write( "DEFAULT_TECHBASE=" + o.DefaultTechbase );
        FR.newLine();
        FR.write( "ALLOW_RBLADE=" + GetBoolean( o.Equip_AllowRBlade ) );
        FR.newLine();

        FR.newLine();
        FR.write( "########## Color Options ##########" );
        FR.newLine();
        FR.write( "BG_LOCKED=" + o.bg_LOCKED.getRed() + "," + o.bg_LOCKED.getGreen() + "," + o.bg_LOCKED.getBlue() );
        FR.newLine();
        FR.write( "FG_LOCKED=" + o.fg_LOCKED.getRed() + "," + o.fg_LOCKED.getGreen() + "," + o.fg_LOCKED.getBlue() );
        FR.newLine();
        FR.write( "BG_LINKED=" + o.bg_LINKED.getRed() + "," + o.bg_LINKED.getGreen() + "," + o.bg_LINKED.getBlue() );
        FR.newLine();
        FR.write( "FG_LINKED=" + o.fg_LINKED.getRed() + "," + o.fg_LINKED.getGreen() + "," + o.fg_LINKED.getBlue() );
        FR.newLine();
        FR.write( "BG_EMPTY=" + o.bg_EMPTY.getRed() + "," + o.bg_EMPTY.getGreen() + "," + o.bg_EMPTY.getBlue() );
        FR.newLine();
        FR.write( "FG_EMPTY=" + o.fg_EMPTY.getRed() + "," + o.fg_EMPTY.getGreen() + "," + o.fg_EMPTY.getBlue() );
        FR.newLine();
        FR.write( "BG_NORMAL=" + o.bg_NORMAL.getRed() + "," + o.bg_NORMAL.getGreen() + "," + o.bg_NORMAL.getBlue() );
        FR.newLine();
        FR.write( "FG_NORMAL=" + o.fg_NORMAL.getRed() + "," + o.fg_NORMAL.getGreen() + "," + o.fg_NORMAL.getBlue() );
        FR.newLine();
        FR.write( "BG_HILITE=" + o.bg_HILITE.getRed() + "," + o.bg_HILITE.getGreen() + "," + o.bg_HILITE.getBlue() );
        FR.newLine();
        FR.write( "FG_HILITE=" + o.fg_HILITE.getRed() + "," + o.fg_HILITE.getGreen() + "," + o.fg_HILITE.getBlue() );
        FR.newLine();
        FR.write( "BG_ARMORED=" + o.bg_ARMORED.getRed() + "," + o.bg_ARMORED.getGreen() + "," + o.bg_ARMORED.getBlue() );
        FR.newLine();
        FR.write( "FG_ARMORED=" + o.fg_ARMORED.getRed() + "," + o.fg_ARMORED.getGreen() + "," + o.fg_ARMORED.getBlue() );
        FR.newLine();

        FR.newLine();
        FR.write( "########## Export Options ##########" );
        FR.newLine();
        FR.write( "EXPORT_SORT=" + o.Export_Sort );
        FR.newLine();
        FR.write( "EXPORT_AMMOATEND=" + GetBoolean( o.Export_AmmoAtEnd ) );
        FR.newLine();

        FR.newLine();
        FR.write( "########## Solaris 7 Options ##########" );
        FR.newLine();
        FR.write( "S7CALLSIGN=" + o.S7Callsign );
        FR.newLine();
        FR.write( "S7PASSWORD=" + o.S7Password );
        FR.newLine();
        FR.write( "S7USERID=" + o.S7UserID );
        FR.newLine();

        FR.newLine();
        FR.write( "########## Pathing Options ##########" );
        FR.newLine();
        FR.write( "PATH_SAVELOAD=" + o.SaveLoadPath );
        FR.newLine();
        FR.write( "PATH_HTML=" + o.HTMLPath );
        FR.newLine();
        FR.write( "PATH_TXT=" + o.TXTPath );
        FR.newLine();
        FR.write( "PATH_MEGAMEK=" + o.MegamekPath );
        FR.newLine();

        FR.newLine();
        FR.write( "########## End Of File ##########" );
        FR.newLine();
        FR.write( "EOF" );
        FR.close();
    }

    private void ProcessString( String p ) {
        // skip empty lines.
        if( ! p.isEmpty() ) {
            if( p.charAt( 0 ) != '#' ) {
                // this just ignore comments.  Anything else is handled
                StringTokenizer T = new StringTokenizer( p, "=", false );
                if( T.countTokens() == 2 ) {
                    // here is an option we can use.
                    String Name = T.nextToken();
                    String Value = T.nextToken();
                    HTOptions.put( Name, Value );
                }
            }
        }
    }

    private void SetOptions( Options o ) {
        // sets options in the options class given
        if( HTOptions.get( "ARMOR_USE_CUSTOM" ) != null ) {
            o.Armor_CustomPercentage = ProcessBoolean( HTOptions.get( "ARMOR_USE_CUSTOM" ) );
        }
        if( o.Armor_CustomPercentage ) {
            if( HTOptions.get( "ARMOR_CTR" ) != null ) {
                o.Armor_CTRPercent = ProcessInt( HTOptions.get( "ARMOR_CTR" ) );
            }
            if( HTOptions.get( "ARMOR_STR" ) != null ) {
                o.Armor_STRPercent = ProcessInt( HTOptions.get( "ARMOR_STR" ) );
            }
        }

        if( HTOptions.get( "REMOVE_JUMP" ) != null ) {
            o.Heat_RemoveJumps = ProcessBoolean( HTOptions.get( "REMOVE_JUMP" ) );
        }
        if( o.Heat_RemoveJumps ) {
            if( HTOptions.get( "REMOVE_RUN" ) != null ) {
                o.Heat_RemoveMovement = ProcessBoolean( HTOptions.get( "REMOVE_RUN" ) );
            }
        }
        if( HTOptions.get( "REMOVE_STEALTH" ) != null ) {
            o.Heat_RemoveStealthArmor = ProcessBoolean( HTOptions.get( "REMOVE_STEALTH" ) );
        }

        if( HTOptions.get( "REMOVE_OS" ) != null ) {
            o.Heat_RemoveOSWeapons = ProcessBoolean( HTOptions.get( "REMOVE_OS" ) );
        }
        if( HTOptions.get( "REMOVE_REAR" ) != null ) {
            o.Heat_RemoveRearWeapons = ProcessBoolean( HTOptions.get( "REMOVE_REAR" ) );
        }
        if( HTOptions.get( "UAC_RAC_FULLRATE" ) != null ) {
            o.Heat_UAC_RAC_FullRate = ProcessBoolean( HTOptions.get( "UAC_RAC_FULLRATE") );
        }
        if( HTOptions.get( "REMOVE_EQUIPMENT" ) != null ) {
            o.Heat_RemoveEquipment = ProcessBoolean( HTOptions.get( "REMOVE_EQUIPMENT" ) );
        }

        if( HTOptions.get( "ARMOR_HEAD_ALLOC" ) != null ) {
            o.Armor_Head = ProcessInt( HTOptions.get( "ARMOR_HEAD_ALLOC" ) );
        }
        if( HTOptions.get( "ARMOR_PRIORITY" ) != null ) {
            o.Armor_Priority = ProcessInt( HTOptions.get( "ARMOR_PRIORITY" ) );
        }

        if( HTOptions.get( "DEFAULT_RULES" ) != null ) {
            o.DefaultRules = ProcessInt( HTOptions.get( "DEFAULT_RULES" ) );
        }
        if( HTOptions.get( "DEFAULT_ERA" ) != null ) {
            o.DefaultEra = ProcessInt( HTOptions.get( "DEFAULT_ERA" ) );
        }
        if( HTOptions.get( "DEFAULT_TECHBASE" ) != null ) {
            o.DefaultTechbase = ProcessInt( HTOptions.get( "DEFAULT_TECHBASE" ) );
        }
        if( HTOptions.get( "ALLOW_RBLADE" ) != null ) {
            o.Equip_AllowRBlade = ProcessBoolean( HTOptions.get( "ALLOW_RBLADE" ) );
        }

        if( HTOptions.get( "BG_LOCKED" ) != null ) {
            o.bg_LOCKED = ProcessColor( HTOptions.get( "BG_LOCKED" ) );
        }
        if( HTOptions.get( "FG_LOCKED" ) != null ) {
            o.fg_LOCKED = ProcessColor( HTOptions.get( "FG_LOCKED" ) );
        }
        if( HTOptions.get( "BG_LINKED" ) != null ) {
            o.bg_LINKED = ProcessColor( HTOptions.get( "BG_LINKED" ) );
        }
        if( HTOptions.get( "FG_LINKED" ) != null ) {
            o.fg_LINKED = ProcessColor( HTOptions.get( "FG_LINKED" ) );
        }
        if( HTOptions.get( "BG_EMPTY" ) != null ) {
            o.bg_EMPTY = ProcessColor( HTOptions.get( "BG_EMPTY" ) );
        }
        if( HTOptions.get( "FG_EMPTY" ) != null ) {
            o.fg_EMPTY = ProcessColor( HTOptions.get( "FG_EMPTY" ) );
        }
        if( HTOptions.get( "BG_ARMORED" ) != null ) {
            o.bg_ARMORED = ProcessColor( HTOptions.get( "BG_ARMORED" ) );
        }
        if( HTOptions.get( "FG_ARMORED" ) != null ) {
            o.fg_ARMORED = ProcessColor( HTOptions.get( "FG_ARMORED" ) );
        }
        if( HTOptions.get( "BG_NORMAL" ) != null ) {
            o.bg_NORMAL = ProcessColor( HTOptions.get( "BG_NORMAL" ) );
        }
        if( HTOptions.get( "FG_NORMAL" ) != null ) {
            o.fg_NORMAL = ProcessColor( HTOptions.get( "FG_NORMAL" ) );
        }
        if( HTOptions.get( "BG_HILITE" ) != null ) {
            o.bg_HILITE = ProcessColor( HTOptions.get( "BG_HILITE" ) );
        }
        if( HTOptions.get( "FG_HILITE" ) != null ) {
            o.fg_HILITE = ProcessColor( HTOptions.get( "FG_HILITE" ) );
        }

        if( HTOptions.get( "EXPORT_SORT" ) != null ) {
            o.Export_Sort = ProcessInt( HTOptions.get( "EXPORT_SORT" ) );
        }
        if( HTOptions.get( "EXPORT_AMMOATEND" ) != null ) {
            o.Export_AmmoAtEnd = ProcessBoolean( HTOptions.get( "EXPORT_AMMOATEND" ) );
        }

        if( HTOptions.get( "S7CALLSIGN" ) != null ) {
            o.S7Callsign = HTOptions.get( "S7CALLSIGN" );
        }
        if( HTOptions.get( "S7PASSWORD" ) != null ) {
            o.S7Password = HTOptions.get( "S7PASSWORD" );
        }
        if( HTOptions.get( "S7USERID" ) != null ) {
            o.S7UserID = ProcessInt( HTOptions.get( "S7USERID" ) );
        }

        if( HTOptions.get( "PATH_SAVELOAD" ) != null ) {
            o.SaveLoadPath = HTOptions.get( "PATH_SAVELOAD" );
        }
        if( HTOptions.get( "PATH_HTML" ) != null ) {
            o.HTMLPath = HTOptions.get( "PATH_HTML" );
        }
        if( HTOptions.get( "PATH_TXT" ) != null ) {
            o.TXTPath = HTOptions.get( "PATH_TXT" );
        }
        if( HTOptions.get( "PATH_MEGAMEK" ) != null ) {
            o.MegamekPath = HTOptions.get( "PATH_MEGAMEK" );
        }
    }

    private boolean ProcessBoolean( String s ) {
        //  parses a String into a boolean
        if( s.compareToIgnoreCase( "false" ) == 0 ) {
            return false;
        } else if( s.compareToIgnoreCase( "true" ) == 0 ) {
            return true;
        } else {
            return false;
        }
    }

    private int ProcessInt( String s ) {
        // attempts to process an integer out fo a String
        try{
            int i = Integer.parseInt( s ) ;
            return i;
        } catch( NumberFormatException n ) {
            // couldn't get anything out of it.  return 0;
            return 0;
        }
    }

    private Color ProcessColor( String s ) {
        StringTokenizer T = new StringTokenizer( s, ",", false );
        if( T.countTokens() != 3 ) {
            return new Color( 0, 0, 0 );
        } else {
            return new Color( Integer.parseInt( T.nextToken() ), Integer.parseInt( T.nextToken() ), Integer.parseInt( T.nextToken() ) );
        }
    }

    private String GetBoolean( boolean b ) {
        if( b ) {
            return "TRUE";
        } else {
            return "FALSE";
        }
    }
}

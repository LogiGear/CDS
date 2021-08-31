import React from 'react'

import { CKEditor } from '@ckeditor/ckeditor5-react';


//import Context from '@ckeditor/ckeditor5-core/src/context';
//import ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import DecoupledEditor from '@ckeditor/ckeditor5-build-decoupled-document';
// import ClassicEditor from '@ckeditor/ckeditor5-editor-classic/src/classiceditor';





const CKEditors = () => {
    const editor = null;

    return (
        <div className="App">
            <h2>CKEditor 5 using a custom build - decoupled editor</h2>
            <CKEditor
                onReady={editor => {
                    console.log('Editor is ready to use!', editor);

                    // Insert the toolbar before the editable area.
                    editor.ui.getEditableElement().parentElement.insertBefore(
                        editor.ui.view.toolbar.element,
                        editor.ui.getEditableElement()
                    );
                    editor.plugins.get("FileRepository").createUploadAdapter = function(loader) {
                        return new UploadAdapter(loader);
                     };

                    editor = editor;
                }}
                onError={({ willEditorRestart }) => {
                   
                    if (willEditorRestart) {
                        editor.ui.view.toolbar.element.remove();
                    }
                }}
                onChange={(event, editor) => {
                    
                    console.log(editor.getData())
                }}
                editor={DecoupledEditor}
                data="<p>Hello from CKEditor 5's decoupled editor!</p>"
               
                config={{
                  
                    // toolbar: ['heading', '|', 'bold', 'italic', 'link', 'bulletedList', 'numberedList', 'blockQuote','uploadImage'],
                    // heading: {
                    //     options: [
                    //         { model: 'paragraph', title: 'Paragraph', class: 'ck-heading_paragraph' },
                    //         { model: 'heading1', view: 'h1', title: 'Heading 1', class: 'ck-heading_heading1' },
                    //         { model: 'heading2', view: 'h2', title: 'Heading 2', class: 'ck-heading_heading2' }
                    //     ]
                    // },
                    // ckfinder: {
                    //     // Upload the images to the server using the CKFinder QuickUpload command.
                    //     uploadUrl: ''
                    //   }
                }}
            />
        </div>
    )
}

export default CKEditors

class UploadAdapter {

	constructor( loader ) {
		
		this.loader = loader;
	}

	
	upload() {
		return new Promise( ( resolve, reject ) => {
			const reader = this.reader = new window.FileReader();

			reader.addEventListener( 'load', () => {
				resolve( { default: reader.result } );
			} );

			reader.addEventListener( 'error', err => {
				reject( err );
			} );

			reader.addEventListener( 'abort', () => {
				reject();
			} );

			this.loader.file.then( file => {
				reader.readAsDataURL( file );
			} );
		} );
	}

	
	abort() {
		this.reader.abort();
	}
}


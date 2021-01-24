 import React from 'react';
 import './styles.scss';

const NavBar = () =>(
    <nav className="row bg-primary main-nav">
        <div className="col-2">
            <a href="link" className="nav-logo-text">
                <h4>DS Catalog</h4>
            </a>
        </div>
        <div className="col-6">
            <ul className="main-menu">
                <li>
                    <a href="link">
                        HOME
                    </a>
                </li>
                <li>
                    <a href="link">
                            CATALOG
                    </a>             
                </li>
                <li>
                    <a href="link">
                        ADMIN
                    </a>
                </li>

            </ul>
        </div>
    </nav>
)

export default NavBar;
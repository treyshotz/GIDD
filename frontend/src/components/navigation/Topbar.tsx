import { useMemo, useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import URLS from 'URLS';
import classNames from 'classnames';
import { useUser, useIsAuthenticated, useLogout } from 'hooks/User';

// Material UI Components
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Hidden from '@material-ui/core/Hidden';
import Button from '@material-ui/core/Button';
import IconButton from '@material-ui/core/IconButton';

// Assets/Icons
import MenuIcon from '@material-ui/icons/MenuRounded';
import CloseIcon from '@material-ui/icons/CloseRounded';

// Project Components
import Sidebar from 'components/navigation/Sidebar';
import Logo from 'components/miscellaneous/Logo';

const useStyles = makeStyles((theme) => ({
  appBar: {
    boxSizing: 'border-box',
    backgroundColor: theme.palette.colors.topbar,
    color: theme.palette.text.primary,
    flexGrow: 1,
    zIndex: theme.zIndex.drawer + 1,
    transition: '0.4s',
  },
  toolbar: {
    width: '100%',
    maxWidth: theme.breakpoints.values.xl,
    margin: 'auto',
    padding: theme.spacing(0, 1),
    display: 'grid',
    gridTemplateColumns: '120px 1fr 120px',
    [theme.breakpoints.down('md')]: {
      gridTemplateColumns: '80px 1fr',
    },
  },
  logo: {
    height: 45,
    width: 'auto',
    marginLeft: 0,
  },
  items: {
    display: 'flex',
    justifyContent: 'flex-start',
    color: theme.palette.common.white,
  },
  right: {
    display: 'flex',
    justifyContent: 'flex-end',
  },
  menuButton: {
    color: theme.palette.common.white,
    margin: 'auto 0',
  },
  selected: {
    borderBottom: '2px solid ' + theme.palette.common.white,
  },
  profileName: {
    margin: `auto ${theme.spacing(1)}px`,
    color: theme.palette.common.white,
    textAlign: 'right',
    overflow: 'hidden',
    textOverflow: 'ellipsis',
    whiteSpace: 'nowrap',
  },
  topbarItem: {
    alignSelf: 'center',
  },
  authButton: {
    color: theme.palette.get<string>({ light: theme.palette.common.white, dark: theme.palette.common.black }),
    borderColor: theme.palette.get<string>({ light: theme.palette.common.white, dark: theme.palette.common.black }),
    '&:hover': {
      borderColor: theme.palette.get<string>({ light: '#cccccc', dark: '#333333' }),
    },
  },
}));

export type TopBarItemProps = {
  text: string;
  to: string;
};

const TopBarItem = ({ text, to }: TopBarItemProps) => {
  const classes = useStyles({});
  const selected = useMemo(() => location.pathname === to, [location.pathname, to]);
  return (
    <div className={classNames(classes.topbarItem, selected && classes.selected)}>
      <Button color='inherit' component={Link} onClick={selected ? () => window.location.reload() : undefined} to={to} variant='text'>
        {text}
      </Button>
    </div>
  );
};

const Topbar = () => {
  const isAuthenticated = useIsAuthenticated();
  const { data: user } = useUser();
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const classes = useStyles();
  const logout = useLogout();

  useEffect(() => {
    window.scrollTo(0, 0);
  }, []);

  const items = useMemo(
    () =>
      [
        { text: 'Om GIDD', to: URLS.LANDING },
        { text: 'Aktiviteter', to: URLS.ACTIVITIES },
        isAuthenticated ? { text: 'Innlogget', to: URLS.LOGIN } : { text: 'Ikke innlogget', to: URLS.LANDING },
      ] as Array<TopBarItemProps>,
    [isAuthenticated],
  );

  return (
    <AppBar className={classes.appBar} color='primary' elevation={0} position='fixed'>
      <Toolbar disableGutters>
        <div className={classes.toolbar}>
          <Link to={URLS.LANDING}>
            <Logo className={classes.logo} size='large' />
          </Link>
          <Hidden mdDown>
            <div className={classes.items}>
              {items.map((item, i) => (
                <TopBarItem key={i} {...item} />
              ))}
            </div>
          </Hidden>
          <div className={classes.right}>
            <Hidden mdDown>
              {user ? (
                <Button className={classes.authButton} onClick={logout} variant='outlined'>
                  Logg ut
                </Button>
              ) : (
                <Button className={classes.authButton} component={Link} to={URLS.LOGIN} variant='outlined'>
                  Logg inn
                </Button>
              )}
            </Hidden>
            <Hidden mdUp>
              <IconButton className={classes.menuButton} onClick={() => setSidebarOpen((prev) => !prev)}>
                {sidebarOpen ? <CloseIcon /> : <MenuIcon />}
              </IconButton>
              <Sidebar items={items} onClose={() => setSidebarOpen(false)} open={sidebarOpen} />
            </Hidden>
          </div>
        </div>
      </Toolbar>
    </AppBar>
  );
};

export default Topbar;

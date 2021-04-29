// Material UI Components
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles((theme) => ({
  logo: {
    margin: 'auto',
    marginTop: theme.spacing(0.5),
    display: 'flex',
    height: '100%',
    width: '100%',
    enableBackground: 'new 0 0 500 500',
  },
  outer: { fill: theme.palette.get<string>({ light: '#bfc7f0', dark: '#372729' }) },
  middle: { fill: theme.palette.get<string>({ light: '#dddddd', dark: '#a4a4a4' }) },
  inner: { fill: theme.palette.get<string>({ light: '#a4a399', dark: '#231d31' }) },
}));

const LogoIcon = () => {
  const classes = useStyles();

  return (
    <svg
      aria-label='GIDD sin logo'
      className={classes.logo}
      id='GiddLogo'
      version='1.1'
      viewBox='0 0 500 500'
      x='0px'
      xmlns='http://www.w3.org/2000/svg'
      xmlSpace='preserve'
      y='0px'>
      <g>
        <g>
          <circle className={classes.outer} cx='250' cy='250' r='250' />
        </g>
        <circle className={classes.middle} cx='250.5' cy='249.5' r='156.5' />
        <circle className={classes.inner} cx='250.5' cy='249.5' r='61.5' />
      </g>
    </svg>
  );
};
export default LogoIcon;

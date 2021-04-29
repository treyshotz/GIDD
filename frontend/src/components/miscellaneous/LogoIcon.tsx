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
  st0: { fill: '#FFFFFF' },
  st1: { fill: '#B70000' },
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
          <circle cx='250' cy='250' r='250' />
        </g>
        <circle className={classes.st0} cx='250.5' cy='249.5' r='156.5' />
        <circle className={classes.st1} cx='250.5' cy='249.5' r='61.5' />
      </g>
    </svg>
  );
};
export default LogoIcon;
